package com.example.nicejobapplication.fragment

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.CorpAdapter
import com.example.nicejobapplication.databinding.FragmentCorpBinding
import com.example.nicejobapplication.modal.Corporation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CorpFragment : Fragment() {

    private lateinit var rvCorporation: RecyclerView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_corp, container, false)
        var list = mutableListOf<Corporation>()
        var corpArrayList: ArrayList<Corporation> = ArrayList()
//        var corpName = "NGÂN HÀNG THƯƠNG MẠI CỔ PHẦN KỸ THƯƠNG VIỆT NAM"
//        var corpDescription = "GIỚI THIỆU CÔNG TY:  Techcombank mang sứ mệnh dẫn dắt hành trình số hóa của ngành tài chính, tạo động lực cho mỗi cá nhân, doanh nghiệp và tổ chức phát triển bền vững và bứt phá thành công.  Được thành lập vào tháng 9 năm 1993 và có trụ sở chính tại Hà Nội, Techcombank là một trong những ngân hàng thương mại cổ phần lớn nhất tại Việt Nam và là một trong những tổ chức ngân hàng hàng đầu tại Châu Á. Chúng tôi có hơn 12.000 nhân viên và 9.6 triệu khách hàng bán lẻ và khách hàng doanh nghiệp tại Việt Nam. Mạng lưới rộng khắp của chúng tôi gồm 309 chi nhánh và phòng giao dịch trên 45 tỉnh thành. Techcombank là ngân hàng thương mại tư nhân đầu tiên tại Việt Nam gia nhập “câu lạc bộ tỷ đô” năm 2021, với lợi nhuận trước thuế (LNTT) đạt 23,2 nghìn tỷ đồng, tăng 47,1% so với 2020. Đây là năm thứ năm liên tiếp Techcombank ghi nhận lợi nhuận tăng trưởng hai chữ số. Ngân hàng cũng ghi nhận tốc độ tăng trưởng kép lợi nhuận trong giai đoạn 2016-2021 với mức kỷ lục 50%/năm.  Bằng cách không ngừng truyền cảm hứng cho mỗi tài năng của chúng tôi để ‘Dare to Be a Greater You’, chúng tôi đặt mục tiêu trở thành ngân hàng số 1 Việt Nam với vốn hóa 20 tỷ USD.  BỔ SUNG: THÔNG ĐIỆP TECHCOMBANK:  Đại dịch Covid-19 làm đảo lộn nền kinh tế thế giới với những hệ quả cắt giảm ồ ạt nhưng không vì thế mà Ngân hàng cắt giảm chi phí đầu tư vào việc đào tạo và phát triển Con Người. Với 836.456 số giờ đào tạo trực tuyến trong 2021, Techcombank lọt top 6% công ty có chỉ số cao nhất về Hiệu quả Nhân viên trong khu vực Asean. Vừa qua, Chương trình Phát triển Năng lực Lãnh đạo được thiết kế thành công dực theo 5 cấp độ lãnh đạo tại Ngân hàng. Cùng với đó là sự đầu tư triển khai rất nhiều khóa đào tạo nghiệp vụ và lãnh đạo khác được thiết kế riêng cho từng cấp độ quản lý như: TechcomLeader, Future Leaders Program, TechcomHero, vv.  Bên cạnh Con người, Dữ liệu & Số hóa là những trụ cột chiến lược giúp Techcombank có được chỗ đứng trên thị trường như ngày hôm nay. Với Ngân hàng, Dữ liệu là kim chỉ nam cần thiết giúp khách hàng có những lựa chọn sáng suốt và giúp ngân hàng tạo ra các giải pháp phù hợp với nhu cầu của từng cá nhân. Ngân hàng tập trung ứng dụng công nghệ thông tin trong mọi hoạt động, đưa 90% ứng dụng trù định lên đám mây, gia tăng tổng lượng giao dịch số lên 70% (đạt 652 triệu), và tăng trưởng 32,3% lượng khách hàng số trong 2021.  “Văn hóa tổ chức” được xây dựng dựa trên 5 Giá trị cốt lõi đã tạo nên sức mạnh của Techcombank và là nền tảng cho sự phát triển bền vững của tổ chức, mang lại thành công cho khách hàng.  Khách hàng là trọng tâm: Vì chúng ta chỉ thành công khi khách hàng thành công.  Đổi mới và sáng tạo: Để luôn dẫn đầu.  Hợp tác vì mục tiêu chung: Tạo nên sức mạnh tập thể để mang lại kết quả vượt trội cho cá nhân và tổ chức.  Phát triển bản thân: Để có khả năng nắm bắt cơ hội phát triển cùng tổ chức.  Làm việc hiệu quả: Để mang lại thành công lớn hơn với nguồn lực phù hợp."
//        var corp = Corporation(corpName = corpName, corpDescription = corpDescription)
//
//        corpName = "NGÂN HÀNG THƯƠNG MẠI CỔ PHẦN KỸ THƯƠNG VIỆT NAM"
//        corpDescription = "adaksdjaklsdjklasdhjklahdlJKAhsdjkashdkjhSJKdfhajklsdhfjkadshfjkahsdfjkhaksdjfh"
//        var corp2 = Corporation(corpName = corpName, corpDescription = corpDescription)
//
//        corpName = "NGÂN HÀNG THƯƠNG MẠI CỔ PHẦN KỸ THƯƠNG VIỆT NAM"
//        corpDescription = "GIỚI THIỆU CÔNG TY:  Techcombank mang sứ mệnh dẫn dắt hành trình số hóa của ngành tài chính, tạo động lực cho mỗi cá nhân, doanh nghiệp và tổ chức phát triển bền vững và bứt phá thành công.  Được thành lập vào tháng 9 năm 1993 và có trụ sở chính tại Hà Nội, Techcombank là một trong những ngân hàng thương mại cổ phần lớn nhất tại Việt Nam và là một trong những tổ chức ngân hàng hàng đầu tại Châu Á. Chúng tôi có hơn 12.000 nhân viên và 9.6 triệu khách hàng bán lẻ và khách hàng doanh nghiệp tại Việt Nam. Mạng lưới rộng khắp của chúng tôi gồm 309 chi nhánh và phòng giao dịch trên 45 tỉnh thành. Techcombank là ngân hàng thương mại tư nhân đầu tiên tại Việt Nam gia nhập “câu lạc bộ tỷ đô” năm 2021, với lợi nhuận trước thuế (LNTT) đạt 23,2 nghìn tỷ đồng, tăng 47,1% so với 2020. Đây là năm thứ năm liên tiếp Techcombank ghi nhận lợi nhuận tăng trưởng hai chữ số. Ngân hàng cũng ghi nhận tốc độ tăng trưởng kép lợi nhuận trong giai đoạn 2016-2021 với mức kỷ lục 50%/năm.  Bằng cách không ngừng truyền cảm hứng cho mỗi tài năng của chúng tôi để ‘Dare to Be a Greater You’, chúng tôi đặt mục tiêu trở thành ngân hàng số 1 Việt Nam với vốn hóa 20 tỷ USD.  BỔ SUNG: THÔNG ĐIỆP TECHCOMBANK:  Đại dịch Covid-19 làm đảo lộn nền kinh tế thế giới với những hệ quả cắt giảm ồ ạt nhưng không vì thế mà Ngân hàng cắt giảm chi phí đầu tư vào việc đào tạo và phát triển Con Người. Với 836.456 số giờ đào tạo trực tuyến trong 2021, Techcombank lọt top 6% công ty có chỉ số cao nhất về Hiệu quả Nhân viên trong khu vực Asean. Vừa qua, Chương trình Phát triển Năng lực Lãnh đạo được thiết kế thành công dực theo 5 cấp độ lãnh đạo tại Ngân hàng. Cùng với đó là sự đầu tư triển khai rất nhiều khóa đào tạo nghiệp vụ và lãnh đạo khác được thiết kế riêng cho từng cấp độ quản lý như: TechcomLeader, Future Leaders Program, TechcomHero, vv.  Bên cạnh Con người, Dữ liệu & Số hóa là những trụ cột chiến lược giúp Techcombank có được chỗ đứng trên thị trường như ngày hôm nay. Với Ngân hàng, Dữ liệu là kim chỉ nam cần thiết giúp khách hàng có những lựa chọn sáng suốt và giúp ngân hàng tạo ra các giải pháp phù hợp với nhu cầu của từng cá nhân. Ngân hàng tập trung ứng dụng công nghệ thông tin trong mọi hoạt động, đưa 90% ứng dụng trù định lên đám mây, gia tăng tổng lượng giao dịch số lên 70% (đạt 652 triệu), và tăng trưởng 32,3% lượng khách hàng số trong 2021.  “Văn hóa tổ chức” được xây dựng dựa trên 5 Giá trị cốt lõi đã tạo nên sức mạnh của Techcombank và là nền tảng cho sự phát triển bền vững của tổ chức, mang lại thành công cho khách hàng.  Khách hàng là trọng tâm: Vì chúng ta chỉ thành công khi khách hàng thành công.  Đổi mới và sáng tạo: Để luôn dẫn đầu.  Hợp tác vì mục tiêu chung: Tạo nên sức mạnh tập thể để mang lại kết quả vượt trội cho cá nhân và tổ chức.  Phát triển bản thân: Để có khả năng nắm bắt cơ hội phát triển cùng tổ chức.  Làm việc hiệu quả: Để mang lại thành công lớn hơn với nguồn lực phù hợp."
//        var corp3 = Corporation(corpName = corpName, corpDescription = corpDescription)
//
//        corpArrayList.add(corp)
//        corpArrayList.add(corp2)
//        corpArrayList.add(corp3)
        db = FirebaseFirestore.getInstance()

        db.collection("corporations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result.documents) {
                    val corpName = document.data?.get("corpName")
                    val corpDescription = document.data?.get("corpDescription")
                    val corp = Corporation(corpName = corpName as String, corpDescription = corpDescription as String)
                    list.add(corp)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        corpArrayList.addAll(list)

        rvCorporation = view.findViewById(R.id.rvCorp)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        rvCorporation.layoutManager = linearLayoutManager
        rvCorporation.adapter = CorpAdapter(this@CorpFragment.requireContext(), corpArrayList)

        return view
    }


}