package com.rooplor.classcraftbackend.dtos

data class FeedbackResponse(
    var userDetail: UserDetailDTO,
    var feedbackResponse: Map<String, Any>,
)
