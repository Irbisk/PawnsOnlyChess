package chess

enum class Pawn(val color: Char, val direction: Int) {
    BLACK('B', 1), WHITE('W', -1), NONE(' ', 0);
}