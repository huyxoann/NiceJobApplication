package com.example.nicejobapplication.modal

import com.google.firebase.Timestamp

class Jobs(
    var jobID: String,
    var jobName: String,
    var corpID: String,

    var numOfRecruit: Int,
    var genderJob: Int,
    var workAddress: Array<String>,

    var careerId: Int,
    var expId: Int,
    var wayToWorkId: Int,
    var salaryId: Int,
    var levelId: Int,

    var state: Int,
    var createdAt: Timestamp?,
    var expertDay: Timestamp?
)
{
    constructor(jobID: String, jobName: String, corpID: String, expId: Int, salaryId: Int, workAddress: Array<String>) :
            this(jobID, jobName, corpID, 0, 0, workAddress, 0, expId, 0, salaryId, 0, 0 , null, null)


}