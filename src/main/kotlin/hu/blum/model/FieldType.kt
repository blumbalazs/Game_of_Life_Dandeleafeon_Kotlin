package hu.blum.model

enum class FieldType {
    DEAD_CELL,
    LIVE_CELL,
    WALL,
    FINNISH,
    DANDELIFEON;

    companion object{
        fun fromInt(i:Int) = FieldType.values().first{it.ordinal == i}
    }

}