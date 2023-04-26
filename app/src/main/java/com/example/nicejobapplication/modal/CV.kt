package com.example.nicejobapplication.modal

data class CV(
    var cvId: String,
    var cvName: String,
    var avatar: String,
    var employerName: String,
    var jobPosition: String,
    var email: String,
    var phoneNumber: String,
    var gentle: String,
    var address: String,

    //need edit----------------------
//    dayOfBirth: Timestamp,
    var dayOfBirth: String,

//    skill: Array<String>,
    var careerGoal: String,
    var salary: String,
    var introduceYourself: String,
//    hobby: Array<String>, //nếu có
//    workExperience: Array<String>,
//    academicLevel: Array<String>,
    var workExperience: String,
    var academicLevel: String,
//    activity: Array<String>, //nếu có

//    careerId: Int,
//    expId: Int,
//    employeeId: String,
//    createdAt: Timestamp
) {
}
