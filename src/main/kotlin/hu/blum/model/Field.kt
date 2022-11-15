package hu.blum.model

data class Coordinate(val X: Int,val y: Int)
class Field (val coordinate: Coordinate, var type: FieldType){
}