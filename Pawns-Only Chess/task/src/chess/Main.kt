package chess


fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val FIRST_PLAYER = readln()
    println("Second Player's name:")
    val SECOND_PLAYER = readln()
    board.setFiguresForStart()
    board.printBoard()
    val game = Game(FIRST_PLAYER, SECOND_PLAYER)
    game.play()
}

