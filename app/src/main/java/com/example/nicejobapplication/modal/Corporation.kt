package com.example.nicejobapplication.modal

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
    constructor(corpName: String, corpDescription: String) : this("", corpName, "", corpDescription, "", "", 0, "")

    var corpName = corpName
    var corpDescription = corpDescription
}