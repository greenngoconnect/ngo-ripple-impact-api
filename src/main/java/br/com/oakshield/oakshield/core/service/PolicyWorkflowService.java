package br.com.oakshield.oakshield.core.service;

import java.util.UUID;

public interface PolicyWorkflowService {
    SubmitReviewResponse submitReview(UUID policyId, SubmitReviewRequest request);
    PolicyApproveResponse approve(UUID policyId, PolicyApproveRequest request);
    PolicyPublishResponse publish(UUID policyId, PolicyPublishRequest request);
}
