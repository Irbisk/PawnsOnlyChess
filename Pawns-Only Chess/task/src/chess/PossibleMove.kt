package chess

class PossibleMove(val toY: Int, val toX: Int)

fun getListOfPossibleMoves(pawnWithYX: PawnWithYX): MutableList<PossibleMove> {
    val list = mutableListOf<PossibleMove>()
    val pawnY = pawnWithYX.y
    val pawnX = pawnWithYX.x
    val direction = pawnWithYX.pawn.direction

    val possibleMove1 = PossibleMove(pawnY + (direction * 1), pawnX)
    val possibleMove2 = PossibleMove(pawnY + (direction * 2), pawnX)
    val possibleMove3 = PossibleMove(pawnY + (direction * 1), pawnX - 1)
    val possibleMove4 = PossibleMove(pawnY + (direction * 1), pawnX + 1)
    list.add(possibleMove1)
    list.add(possibleMove2)
    list.add(possibleMove3)
    list.add(possibleMove4)
    return list
}