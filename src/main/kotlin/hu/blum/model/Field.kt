package hu.blum.model

data class Field (var type:FieldType, var age:Int){
    fun increaseAge(){
        if(age<100)
            age++
    }
}