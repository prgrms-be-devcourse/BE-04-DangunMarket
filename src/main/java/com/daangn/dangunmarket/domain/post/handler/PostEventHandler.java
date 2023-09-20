package com.daangn.dangunmarket.domain.post.handler;

import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostEventHandler {

    private final S3Uploader s3Uploader;

    public PostEventHandler(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void deleteImage(String fileName) {
        s3Uploader.deleteImage(fileName);
    }

}
