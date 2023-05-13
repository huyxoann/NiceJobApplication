package com.example.nicejobapplication.modal
data class CV(
   val cvId: String?=null,
   val cvName: String?=null,
   val avatar: String?=null,
   val employerName: String?=null,
   val email: String?=null,
   val phoneNumber: String?=null,
   val gentle: String?=null,
   val address: String?=null,
   val dayOfBirth: String?=null,
   val careerGoal: String?=null,
   val workExperience: String?=null,
   val academicLevel: String?=null,
   val createAt:Long = 0
) {
}
//   class CV(
//    cvId: String,
//    cvName: String,
//    avatar: String,
//    employerName: String,
//    email: String,
//    phoneNumber: String,
//    gentle: String,
//    address: String,
//    dayOfBirth: String,
//    careerGoal: String,
//    workExperience: String,
//    academicLevel: String,
//    createAt:Long
//) {
//   constructor(cvId: String,cvName: String,avatar: String,employerName: String,email: String,
//   phoneNumber: String,gentle: String,address: String,dayOfBirth: String,careerGoal: String,workExperience: String,academicLevel: String)
//   :this(cvId,cvName,avatar,employerName,email,phoneNumber,gentle,address,dayOfBirth,careerGoal,workExperience,academicLevel, 0)
//
//   var cvId = cvId
//   var cvName = cvName
//   var avatar = avatar
//   var employerName = employerName
//   var email = email
//   var phoneNumber = phoneNumber
//   var gentle = gentle
//   var address = address
//   var dayOfBirth = dayOfBirth
//   var careerGoal = careerGoal
//   var workExperience = workExperience
//   var academicLevel = academicLevel
//   var createAt = 0
//
//}