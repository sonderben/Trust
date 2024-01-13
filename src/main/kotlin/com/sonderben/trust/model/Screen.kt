package com.sonderben.trust.model

import com.sonderben.trust.constant.Action
import com.sonderben.trust.constant.ScreenEnum

data class Screen (var screen: ScreenEnum, var actions:MutableList<Action>) {

}