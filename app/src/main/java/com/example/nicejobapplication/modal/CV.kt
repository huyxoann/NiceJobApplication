package com.example.nicejobapplication.modal

import com.google.firebase.Timestamp

open class CV(
    cvId: String,
    employerName: String,

    dayOfBirth: Timestamp,
    skill: Array<String>,
    email: String,
    phoneNumber: String,
    hobby: Array<String>, //nếu có
    avatar: String,
    careerGoal: String,
    academicLevel: Array<String>,
    workExperience: Array<String>,
    activity: Array<String>, //nếu có

    cvName: String,
    careerId: Int,
    expId: Int,
    employeeId: String,
    createdAt: Timestamp
) {
}