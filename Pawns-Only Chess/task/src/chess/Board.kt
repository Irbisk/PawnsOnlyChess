package chess

class Board {
    var field = Array(8) { Array(8) { Pawn.NONE } }

    fun setFiguresForStart() {
        for (i in 0..7) {
            field[1][i] = Pawn.BLACK
            field[6][i] = Pawn.WHITE
        }
    }

    fun printBoard() {
        val verticalLine = "|"
        val horizont = "  +---+---+---+---+---+---+---+---+"
        println(horizont)
        var ver = 8
        field.forEach{
            print("${ver--} $verticalLine")
            it.forEach {
                print(" ${it.color} $verticalLine")
            }
            println()
            println(horizont)
        }
        println("    a   b   c   d   e   f   g   h")
    }
}

