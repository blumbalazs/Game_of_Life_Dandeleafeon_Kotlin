package hu.blum.model

class FieldState(var status:FieldType = FieldType.DEAD_CELL, var age:Int = 0)

    val neighbours = mutableListOf<FieldState>()