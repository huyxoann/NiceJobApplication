package com.example.nicejobapplication.modal

import com.google.firebase.Timestamp
import java.time.ZoneId
import java.time.temporal.ChronoUnit

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
    var expertDay: Timestamp?,
    var jobDescription: Array<String>?,
    var recruitRequire: Array<String>?,
    var benefit: Array<String>?
)
{
    constructor(jobID: String, jobName: String, corpID: String, expId: Int, salaryId: Int, workAddress: Array<String>, deadline: Timestamp) :
            this(jobID, jobName, corpID, 0, 0, workAddress, 0, expId, 0, salaryId, 0, 0 , null, deadline, null, null,null)

    fun displayJobDescription(jobDescription: Array<String>?):String{
        var string = ""
        if (jobDescription != null) {
            for(des in jobDescription){
                string += "$des \n"
            }
        }else{
            string = "Chưa cập nhật"
        }
        return string.replace("[", "").replace("]", "")
    }
    fun displayRecruitRequire(recruitRequire: Array<String>?):String{
        var string = ""
        if (recruitRequire != null) {
            for(des in recruitRequire){
                string += "$des \n"
            }
        }else{
            string = "Chưa cập nhật"
        }
        return string.replace("[", "").replace("]", "")
    }
    fun displayBenefit(benefit: Array<String>?):String{
        var string = ""
        if (benefit != null) {
            for(des in benefit){
                string += "$des \n"
            }
        }else{
            string = "Chưa cập nhật"
        }
        return string.replace("[", "").replace("]", "")
    }
    fun displayWorkAddress(workAddress: Array<String>):String{
        var string = ""
        for(address in workAddress){
            string += "$address \n"
        }
        return string
    }
    fun getLevel(levelId: Int): String{
        return when(levelId){
            1 -> "Nhân viên"
            2 -> "Trưởng nhóm"
            3 -> "Trưởng/Phó phòng"
            4 -> "Quản lý / Giám sát"
            5 -> "Trưởng chi nhánh"
            6 -> "Phó giám đốc"
            7 -> "Giám đốc"
            8 -> "Thực tập sinh"
            else -> "Nhân viên"
        }
    }
    fun getGenderRequire(genderId: Int):String{
        return when(genderId){
            0-> "Nam"
            1-> "Nữ"
            else -> "Không yêu cầu"
        }
    }

    fun getWayToWork(wtfId: Int): String {
        return when(wtfId){
            1 -> "Toàn thời gian"
            2 -> "Bán thời gian"
            3 -> "Thực tập"
            else -> "Remote - Làm việc từ xa"
        }
    }


    fun getAddress(address : String): String {
        val splinted = address.split(":")
        return splinted[0].replace("- ", "")
    }


    fun getExp(exp: Int): String{
        return when(exp){
            1 -> "Mới ra trường"
            2 -> "1-2 năm"
            3 -> "3-4 năm"
            4 -> "5 năm trở lên"
            else -> "Không yêu cầu"
        }
    }

    fun getSalary(exp: Int): String{
        return when(exp){
            1 -> "Dưới 3 triệu"
            2 -> "3-5 triệu"
            3 -> "5-7 triệu"
            4 -> "7-10 triệu"
            5 -> "10-12 triệu"
            6 -> "12-15 triệu"
            7 -> "15-20 triệu"
            8 -> "20-25 triệu"
            9 -> "25-30 triệu"
            10 -> "Trên 30 triệu"
            else -> "Thỏa thuân"
        }
    }

    fun getDeadline(deadline: Timestamp): Long {
        val currentTime = Timestamp.now().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val futureTime = deadline.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return currentTime.until(futureTime, ChronoUnit.DAYS)
    }


}