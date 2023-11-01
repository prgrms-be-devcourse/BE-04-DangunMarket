package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.controller.dto.MessageRequest;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.MessageType;
import com.daangn.dangunmarket.domain.chat.repository.chatentry.ChatRoomEntryRedisRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessageResponse;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.domain.post.model.Category;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.repository.category.CategoryRepository;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageMongoRepository chatMessageMongoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ChatRoomInfoRepository chatRoomInfoRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChatRoomEntryRedisRepository chatRoomEntryRedisRepository;

    @MockBean
    private S3Uploader s3Uploader;


    private Member existedSeller;
    private Member existedBuyer;
    private ChatRoom savedChatRoom;

    @BeforeEach
    void setup() {

        chatRoomEntryRedisRepository.deleteAllWithPrefix();
        dataSetup();
    }

    @AfterEach
    void tearDown() {
        chatMessageMongoRepository.deleteAll();
        chatRoomEntryRedisRepository.deleteAllWithPrefix();
    }

    @Test
    @DisplayName("채팅 중 이미지 파일 4개 이상 첨부시 예외가 발생한다.")
    void uploadImages_FileList_ThrowIllegalArgumentException() {
        //when
        List<MultipartFile> multipartFiles = List.of(
                new MockMultipartFile("file1", "imagefile1.jpeg", "image/jpeg", "<<jpeg data>>".getBytes()),
                new MockMultipartFile("file2", "imagefile2.jpeg", "image/jpeg", "<<jpeg data>>".getBytes()),
                new MockMultipartFile("file3", "imagefile3.jpeg", "image/jpeg", "<<jpeg data>>".getBytes()),
                new MockMultipartFile("file4", "imagefile4.jpeg", "image/jpeg", "<<jpeg data>>".getBytes()));

        //then
        assertThatThrownBy(() -> chatService.uploadImages(multipartFiles))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미지 파일 3개 미만일 경우 정상적으로 저장한다.")
    void uploadImages_FileList_ReturnS3Urls() {
        //when
        List<MultipartFile> multipartFiles = List.of(
                new MockMultipartFile("file1", "imagefile1.jpeg", "image/jpeg", "<<jpeg data>>".getBytes()),
                new MockMultipartFile("file2", "imagefile2.jpeg", "image/jpeg", "<<jpeg data>>".getBytes()));
        ImageInfo imageInfo1 = new ImageInfo("1url", "imagefile1");
        ImageInfo imageInfo2 = new ImageInfo("2url", "imagefile2");

        given(s3Uploader.saveImages(any())).willReturn(List.of(
                imageInfo1,
                imageInfo2));

        //when
        List<ImageInfo> results = chatService.uploadImages(multipartFiles);

        //then
        assertThat(results.get(0)).isEqualTo(imageInfo1);
        assertThat(results.get(1)).isEqualTo(imageInfo2);
    }

    @Test
    @DisplayName("채팅 메시지 중 imageUrl이 존재할 경우 MessageType을 IMAGE로 저장한다.")
    void saveMessage_DtoAndIds_ReturnChatMessageResponse() {
        //given
        Long memberId = existedBuyer.getId();
        Long savedRoomId = savedChatRoom.getId();
        MessageRequest request = new MessageRequest("", List.of("사진url1", "사진url2"));

        //when
        ChatMessageResponse chatMessageResponse = chatService.saveMessage(memberId, savedRoomId, request);

        //then
        assertThat(chatMessageResponse.type()).isEqualTo(MessageType.IMAGE);
    }

    @Test
    @DisplayName("채팅 중 상대방이 접속해있다면 채팅의 readCount가 0로 저장된다.(둘 다 읽음)")
    void saveMessage_DtoAndIds_ReturnReadCountZero() {
        //when
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyer.getId().toString());
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedSeller.getId().toString());
        MessageRequest request = new MessageRequest("", List.of("사진url1", "사진url2"));

        //when
        ChatMessageResponse chatMessageResponse = chatService.saveMessage(existedBuyer.getId(), savedChatRoom.getId(), request);

        //then
        Assertions.assertThat(chatMessageResponse.readOrNot()).isEqualTo(0);
    }

    @Test
    @DisplayName("채팅 중 상대방이 접속하지 않았다면 채팅의 readCount가 1로 저장된다.(발송자만 읽음)")
    void saveMessage_DtoAndIds_ReturnReadCountOne() {
        //when
        chatRoomEntryRedisRepository.addMemberToRoom(savedChatRoom.getId().toString(), existedBuyer.getId().toString());
        MessageRequest request = new MessageRequest("", List.of("사진url1", "사진url2"));

        //when
        ChatMessageResponse chatMessageResponse = chatService.saveMessage(existedBuyer.getId(), savedChatRoom.getId(), request);

        //then
        Assertions.assertThat(chatMessageResponse.readOrNot()).isEqualTo(1);

    }

    private void dataSetup() {

        savedChatRoom = chatRoomRepository.save(new ChatRoom());

        existedSeller = memberJpaRepository.save(DataInitializerFactory.member("닉네임1", 35));
        existedBuyer = memberJpaRepository.save(DataInitializerFactory.member("닉네임2", 25));

        Category category = DataInitializerFactory.category();
        Category savedeCategory = categoryRepository.save(category);
        Post savedPost = postRepository.save(DataInitializerFactory.post(existedSeller.getId(), savedeCategory));
        Long existedPostId = savedPost.getId();

        chatRoomInfoRepository.saveAll(
                List.of(
                        DataInitializerFactory.sellerChatRoomInfo(existedPostId, existedSeller.getId(), savedChatRoom),
                        DataInitializerFactory.buyerChatRoomInfo(existedPostId, existedBuyer.getId(), savedChatRoom)
                )
        );
    }

}
