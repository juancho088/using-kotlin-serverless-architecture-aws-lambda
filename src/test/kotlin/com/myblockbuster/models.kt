package com.myblockbuster

import com.myblockbuster.core.BaseModel


class TestModel(var a: String, var b: String, var c: Int, var d: Double): BaseModel() {
    constructor() : this("", "", Int.MAX_VALUE, Double.MAX_VALUE)
}

class TestScenario(var elements: Array<Any>) {

}