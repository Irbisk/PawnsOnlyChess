package chess

import kotlin.math.abs

val board = Board()

class Game(val FIRST_PLAYER: String, val SECOND_PLAYER: String) {

    private var gameIsFinished = false
    private var playerOneTurn = true
    init {
        board.setFiguresForStart()
    }
    var playerOneEnPassant = false
    var playerTwoEnPassant = false

    fun play() {
        while (!gameIsFinished) {
            println("${getPlayerName()}'s turn:")
            val move = readln()
            if (move == "exit") {
                println("Bye!")
                gameIsFinished = true
            } else {
                move(move)
            }
            checkGameIsOver()
        }
    }

    fun move(move: String) {
        if (enteringIsCorrect(move)) {
            val fromX = getIndex(move, 1)
            val fromY = getIndex(move, 2)
            val toX = getIndex(move, 3)
            val toY = getIndex(move, 4)
//            println("FromY = $fromY, FromX = $fromX, toY = $toY, toX = $toX")
            when {
                !pawnExists(fromX, fromY) -> {
                    if (playerOneTurn) {
                        println("No white pawn at ${move.substring(0, 2)}")
                    } else {
                        println("No black pawn at ${move.substring(0, 2)}")
                    }
                }
                !moveIsCorrect(fromY, fromX, toY, toX) -> println("Invalid Input")

                else -> {


                    if (isDiagonalMove(fromY, fromX, toY, toX)) {
                        capture(fromY, fromX, toY, toX)
                    } else {
                        moveChess(fromY, fromX, toY, toX)
                    }
                    checkForEnPassant(fromY, fromX, toY, toX)

                    board.printBoard()
                    playerOneTurn = !playerOneTurn
                }
            }
        } else {
            println("Invalid Input")
        }
    }

    fun moveChess(fromY: Int, fromX: Int, toY: Int, toX: Int) {
        board.field[fromY][fromX] = Pawn.NONE
        if (playerOneTurn) {
            board.field[toY][toX] = Pawn.WHITE
        } else {
            board.field[toY][toX] = Pawn.BLACK
        }
    }

    fun capture(fromY: Int, fromX: Int, toY: Int, toX: Int) {
        if (cellIsEmpty(toX, toY)) {
            if (playerOneTurn) {
                board.field[toY + 1][toX] = Pawn.NONE
            } else {
                board.field[toY - 1][toX] = Pawn.NONE
            }
        }
        moveChess(fromY, fromX, toY, toX)
    }

    fun standartCaptureIsPossible(fromY: Int, fromX: Int, toY: Int, toX: Int): Boolean {
        return if (playerOneTurn) {
            (abs(fromX - toX) == 1 && fromY - toY == 1 && board.field[toY][toX] == Pawn.BLACK)
        } else {
            (abs(fromX - toX) == 1 && fromY - toY == -1 && board.field[toY][toX] == Pawn.WHITE)
        }
    }

    fun checkForEnPassant(fromY: Int, fromX: Int, toY: Int, toX: Int) {
        playerOneEnPassant = false
        playerTwoEnPassant = false
        try {
            if (playerOneTurn) {
                if (fromY == 6) {
                    if (toY - fromY == -2 && (board.field[toY][toX - 1] == Pawn.BLACK || board.field[toY][toX + 1] == Pawn.BLACK)) {
                        playerTwoEnPassant = true
                    }
                }
            } else {
                if (fromY == 1) {
                    if (toY - fromY == 2 && (board.field[toY][toX - 1] == Pawn.WHITE || board.field[toY][toX + 1] == Pawn.WHITE)) {
                        playerOneEnPassant = true
                    }
                }
            }
        } catch (_: IndexOutOfBoundsException) {
        }
    }

    fun checkGameIsOver() {
        var allWhiteCaptured = true
        var allBlackCaptured = true

        board.field.forEach { it.forEach {

            if (it == Pawn.BLACK) {
                allBlackCaptured = false
            }
            if (it == Pawn.WHITE) {
                allWhiteCaptured = false
            }
        } }
        if (allBlackCaptured) {
            println("White Wins!\n" +
                    "Bye!")
            gameIsFinished = true
            return
        }
        if (allWhiteCaptured) {
            println("Black Wins!\n" +
                    "Bye!")
            gameIsFinished = true
            return
        }

        var moveIsPossible = false
        val pawnslist = mutableListOf<PawnWithYX>()
        for (x in 0..7) {
            for (y in 0..7) {
                if (playerOneTurn) {
                    if (board.field[y][x] == Pawn.WHITE) {
                        pawnslist.add(PawnWithYX(Pawn.WHITE, y, x))
                    }
                } else {
                    if (board.field[y][x] == Pawn.BLACK) {
                        pawnslist.add(PawnWithYX(Pawn.BLACK, y, x))
                    }
                }
            }
        }

        pawnslist.forEach {
            val movesList = getListOfPossibleMoves(it)
            val fromY = it.y
            val fromX = it.x
            movesList.forEach {
                try {
                    if (moveIsCorrect(fromY, fromX, it.toY, it.toX)) {
                        moveIsPossible = true
                    }
                } catch (_:ArrayIndexOutOfBoundsException) {}

            }
        }


        if (!moveIsPossible) {
            println("Stalemate!\nBye!")
            gameIsFinished = true
            return
        }
        for (i in 0..7) {
            if (board.field[0][i] == Pawn.WHITE) {
                println("White Wins!\n" +
                        "Bye!")
                gameIsFinished = true
                break
            }
            if (board.field[7][i] == Pawn.BLACK) {
                println("Black Wins!\n" +
                        "Bye!")
                gameIsFinished = true
                break
            }
        }
    }

    fun isDiagonalMove(fromY: Int, fromX: Int, toY: Int, toX: Int): Boolean {
        return if (abs(fromX - toX) == 1 && abs(toY - fromY) == 1) {
            true
        } else false
    }

    fun moveIsCorrect(fromY: Int, fromX: Int, toY: Int, toX: Int):Boolean {
        var correct = true
        if (standartCaptureIsPossible(fromY, fromX, toY, toX)) {
            return correct
        }
        if (playerTwoEnPassant && !playerOneTurn) {
            if (board.field[fromY][toX] == Pawn.WHITE) {
                return correct
            }
        }
        if (playerOneEnPassant && playerOneTurn) {
            if (board.field[fromY][toX] == Pawn.BLACK) {
                return correct
            }
        }
        if (fromX != toX) {
            return false
        }
        if (!cellIsEmpty(toX, toY)) {
            return false
        }
        if (playerOneTurn) {
            if (fromY == 6) {
                if (toY - fromY != -2 && toY - fromY != -1) {
                    correct = false
                }
            } else if (toY - fromY != -1) {
                correct = false
            }
        } else {
            if (fromY == 1) {
                if (toY - fromY != 2 && toY - fromY != 1) {
                    correct = false
                }
            } else if (toY - fromY != 1) {
                correct = false
            }
        }
        return correct
    }

    fun cellIsEmpty(x: Int, y: Int): Boolean {
        return board.field[y][x] == Pawn.NONE
    }

    fun pawnExists(x: Int, y: Int): Boolean {
        return if (playerOneTurn) {
            board.field[y][x] == Pawn.WHITE
        } else board.field[y][x] == Pawn.BLACK
    }

    fun enteringIsCorrect(move: String): Boolean {
        val regex = "[a-h][1-8][a-h][1-8]".toRegex()
        return move.matches(regex)
    }

    fun getIndex(move: String, i: Int): Int {
        val letters = "abcdefgh"
        val digits = "87654321"
        return when (i) {
            1 -> letters.indexOf(move[0])
            2 -> digits.indexOf(move[1])
            3 -> letters.indexOf(move[2])
            4 -> digits.indexOf(move[3])
            else -> 0
        }
    }

    fun getPlayerName(): String {
        return if (playerOneTurn) FIRST_PLAYER else SECOND_PLAYER
    }
}
