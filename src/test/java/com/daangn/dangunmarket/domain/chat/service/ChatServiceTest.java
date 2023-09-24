package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.DataInitializerFactory;
import com.daangn.dangunmarket.domain.chat.controller.dto.MessageRequest;
import com.daangn.dangunmarket.domain.chat.model.ChatMessage;
import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.model.MessageType;
import com.daangn.dangunmarket.domain.chat.repository.chatmessage.ChatMessageMongoRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomJpaRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoJpaRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessageResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.member.repository.MemberJpaRepository;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
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
    private ChatRoomJpaRepository chatRoomJpaRepository;

    @Autowired
    private ChatRoomInfoJpaRepository chatRoomInfoJpaRepository;

    @Autowired
    private ChatMessageMongoRepository chatMessageMongoRepository;

    @MockBean
    private S3Uploader s3Uploader;

    private Member savedMember1;
    private ChatRoom savedRoom1;
    private ChatRoom savedRoom2;

    @BeforeEach
    void setup() {
        dataSetup();
    }

    @AfterEach
    void tearDown() {
        chatMessageMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("member1의 id로 해당 유저가 속한 채팅방들을 조회 후 응답값을 확인한다.")
    void findChatRoomsByMemberId_memberId_ChatRoomsFindResponses() {
        //when
        ChatRoomsFindResponses responses = chatService.findChatRoomsByMemberId(
                savedMember1.getId(),
                PageRequest.of(0, 10)
        );

        //then
        List<ChatRoomsFindResponse> contents = responses.responses().getContent();

        assertThat(contents.get(0).chatRoomId()).isEqualTo(1L);
        assertThat(contents.get(0).latestMessage()).isEqualTo("방 1의 두번째 메세지");
        assertThat(contents.get(0).otherMemberName()).isEqualTo("hany");
        assertThat(contents.get(0).readOrNot()).isEqualTo(1);

        assertThat(contents.get(1).chatRoomId()).isEqualTo(2L);
        assertThat(contents.get(1).latestMessage()).isEqualTo("방 2의 두번째 메세지");
        assertThat(contents.get(1).otherMemberName()).isEqualTo("mac");
        assertThat(contents.get(1).readOrNot()).isEqualTo(0);
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
        Long memberId = savedMember1.getId();
        Long savedRoomId = savedRoom1.getId();
        MessageRequest request = new MessageRequest("", List.of("사진url1", "사진url2"));

        //when
        ChatMessageResponse chatMessageResponse = chatService.saveMessage(memberId, savedRoomId, request);

        //then
        assertThat(chatMessageResponse.type()).isEqualTo(MessageType.IMAGE);
    }

    /**
     * 방     :   해당 방에 참여중인 유저        :  메세지를 보낸 순서
     * room1  : savedMember1, savedMember2   :   Member1, Member2
     * room2  : savedMember1, savedMember3   :   Member3, Member1
     */
    private void dataSetup() {
        savedMember1 = memberJpaRepository.save(
                DataInitializerFactory.member("james", 35)
        );

        Member savedMember2 = memberJpaRepository.save(
                DataInitializerFactory.member("hany", 25)
        );

        Member savedMember3 = memberJpaRepository.save(
                DataInitializerFactory.member("mac", 27)
        );

        savedRoom1 = chatRoomJpaRepository.save(new ChatRoom());
        savedRoom2 = chatRoomJpaRepository.save(new ChatRoom());

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                true,
                1L,
                savedRoom1,
                savedMember1.getId()
        ));

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                false,
                1L,
                savedRoom1,
                savedMember2.getId()
        ));

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                false,
                2L,
                savedRoom2,
                savedMember1.getId()
        ));

        chatRoomInfoJpaRepository.save(new ChatRoomInfo(
                true,
                2L,
                savedRoom2,
                savedMember3.getId()
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom1.getId(),
                savedMember1.getId(),
                "방 1의 첫번째 메세지",
                List.of("a"),
                1,
                MessageType.TALK
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom1.getId(),
                savedMember2.getId(),
                "방 1의 두번째 메세지",
                List.of("a"),
                1,
                MessageType.TALK
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom2.getId(),
                savedMember3.getId(),
                "방 2의 첫번째 메세지",
                List.of("a"),
                1,
                MessageType.TALK
        ));

        chatMessageMongoRepository.save(new ChatMessage(
                savedRoom2.getId(),
                savedMember1.getId(),
                "방 2의 첫번째 메세지",
                List.of("a"),
                1,
                MessageType.TALK
        ));
    }

}
