package hu.bme.aut.model

class FieldModel {
    data class Coord(val x:Int,val y:Int)
    var currentField = HashMap<Coord,Byte>()
        private set

    fun step(){
        TODO()
    }
    fun createLife(x: Int,y:Int){
        TODO()
    }

}