package com.example.nicejobapplication.modal

import android.location.Address
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor


class Corporation(
    corpID: String,
    corpName: String,
    corpLogo: String,
    corpDescription: String,
    website: String,
    address: String,
    corpFieldId: Int,
    employerID: String
)
{
    constructor(corpID: String, corpName: String, corpDescription: String, corpLogo: String) : this(corpID, corpName, corpLogo, corpDescription, "", "", 0, "")
    var corpID = corpID
    var corpName = corpName
    var corpDescription = corpDescription
    var corpLogo = corpLogo

}
