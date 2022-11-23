import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {
    Board chessBoard;

    public static void main(String[] args) {
        try (Scanner s = new Scanner(new File("input.txt"))) {
            int size = s.nextInt();
            Board chessBoard = new Board(size);
            int pieces_count = s.nextInt();
            for (int piece = 0; piece < pieces_count; piece++) {
                String piece_name = s.next();
                String color = s.next();
                int x = s.nextInt();
                int y = s.nextInt();

                // PiecePosition
                PiecePosition pos = new PiecePosition(x, y);

                // PieceColor
                PieceColor piece_color;
                if (color.equals("White")) {
                    piece_color = PieceColor.WHITE;
                } else if (color.equals("Black")) {
                    piece_color = PieceColor.BLACK;
                } else {
                    System.out.println("1");
                    // TODO wrong color
                    piece_color = PieceColor.WHITE; // TODO delete
                }

                // ChessPiece
                if (piece_name.equals("Pawn")) {
                    ChessPiece new_piece = new Pawn(pos, piece_color);
                    chessBoard.addPiece(new_piece);
                } else if (piece_name.equals("King")) {
                    ChessPiece new_piece = new King(pos, piece_color);
                    chessBoard.addPiece(new_piece);
                } else if (piece_name.equals("Knight")) {
                    ChessPiece new_piece = new Knight(pos, piece_color);
                    chessBoard.addPiece(new_piece);
                } else if (piece_name.equals("Rook")) {
                    ChessPiece new_piece = new Rook(pos, piece_color);
                    chessBoard.addPiece(new_piece);
                } else if (piece_name.equals("Queen")) {
                    ChessPiece new_piece = new Queen(pos, piece_color);
                    chessBoard.addPiece(new_piece);
                } else if (piece_name.equals("Bishop")) {
                    ChessPiece new_piece = new Bishop(pos, piece_color);
                    chessBoard.addPiece(new_piece);
                } else {
                    System.out.println("1");
                    // TODO wrong piece type
                }
                Map<String, ChessPiece> ps = chessBoard.positionsToPieces;
                for (Map.Entry<String, ChessPiece> entry : ps.entrySet()) {
                    String key = entry.getKey();
                    ChessPiece value = entry.getValue();
                    System.out.println(value.getPosition());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

class Board {
    int size;
    Map<String, ChessPiece> positionsToPieces = new HashMap<>();

    Board(int boardSize) {
        this.size = boardSize;
    }

    void addPiece(ChessPiece piece) {
        if (piece.getClass() == Knight.class) {
            positionsToPieces.put("Knight", piece);
        } else if (piece.getClass() == King.class) {
            positionsToPieces.put("King", piece);
        } else if (piece.getClass() == Pawn.class) {
            positionsToPieces.put("Pawn", piece);
        } else if (piece.getClass() == Bishop.class) {
            positionsToPieces.put("Bishop", piece);
        } else if (piece.getClass() == Rook.class) {
            positionsToPieces.put("Rook", piece);
        } else if (piece.getClass() == Queen.class) {
            positionsToPieces.put("Queen", piece);
        }
    }

    ChessPiece getPiece(PiecePosition position) {
        for (Map.Entry<String, ChessPiece> entry : positionsToPieces.entrySet()) {
            String key = entry.getKey();
            ChessPiece value = entry.getValue();
            if (value.getPosition() == position) {
                return value;
            }
        }
        return null;
    }
}

class PiecePosition {
    int x, y;

    PiecePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "PiecePosition{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

enum PieceColor {
    WHITE(0), BLACK(1);

    PieceColor(int color) {
    }

    PieceColor parse(String color) {
        if (color.equals("WHITE")) {
            return PieceColor.WHITE;
        } else if (color.equals("BLACK")) {
            return PieceColor.BLACK;
        } else {
            System.out.println("1");
            //TODO An undefined color error
            return PieceColor.WHITE;
        }
    }
}

abstract class ChessPiece {
    PiecePosition position;
    PieceColor color;

    ChessPiece(PiecePosition piecePosition, PieceColor pieceColor) {
        position = piecePosition;
        color = pieceColor;
    }

    PiecePosition getPosition() {
        return this.position;
    }

    PieceColor getColor() {
        return this.color;
    }

    abstract int getMovesCount(Map<String, ChessPiece> positions, int boardSize);

    abstract int getCapturesCount(Map<String, ChessPiece> positions, int boardSize);
}

// TODO recheck if piece is going to the cell where locates same color piece
class Knight extends ChessPiece {
    Knight(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        // possible combinations: (x + 2, j + 1); (x - 2, j + 1); (x + 1, j + 2); (x - 1, j + 2);
        // (x - 2, j - 1); (x + 2, j - 1); (x - 1, j - 2); (x + 1, j - 2)
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        Map<Integer, Integer> captured = new HashMap<>();
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();
            if (x + 2 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 2 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x + 1 == x1 && y + 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 1 == x1 && y + 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 2 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x + 2 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 1 == x1 && y - 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x + 1 == x1 && y - 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            }
        }
        // possible combinations: (x + 2, y + 1); (x - 2, y + 1); (x + 1, y + 2); (x - 1, y + 2);
        // (x - 2, y - 1); (x + 2, y - 1); (x - 1, y - 2); (x + 1, y - 2)
        if (x + 2 <= boardSize && x + 2 >= 0 && y + 1 <= boardSize && y + 1 >= 0) {
            if (!captured.containsKey(x + 2) && !captured.containsValue(y + 1)) {
                ans++;
            }
        }
        if (x - 2 <= boardSize && x - 2 >= 0 && y + 1 <= boardSize && y + 1 >= 0) {
            if (!captured.containsKey(x - 2) && !captured.containsValue(y + 1)) {
                ans++;
            }
        }
        if (x + 1 <= boardSize && x + 1 >= 0 && y + 2 <= boardSize && y + 2 >= 0) {
            if (!captured.containsKey(x + 1) && !captured.containsValue(y + 2)) {
                ans++;
            }
        }
        if (x - 1 <= boardSize && x - 1 >= 0 && y + 2 <= boardSize && y + 2 >= 0) {
            if (!captured.containsKey(x - 1) && !captured.containsValue(y + 2)) {
                ans++;
            }
            // possible combinations: (x + 2, y + 1); (x - 2, y + 1); (x + 1, y + 2); (x - 1, y + 2);
            // (x - 2, y - 1); (x + 2, y - 1); (x - 1, y - 2); (x + 1, y - 2)
        }
        if (x - 2 <= boardSize && x - 2 >= 0 && y - 1 <= boardSize && y - 1 >= 0) {
            if (!captured.containsKey(x - 2) && !captured.containsValue(y - 1)) {
                ans++;
            }
        }
        if (x + 2 <= boardSize && x + 2 >= 0 && y - 1 <= boardSize && y - 1 >= 0) {
            if (!captured.containsKey(x + 2) && !captured.containsValue(y - 1)) {
                ans++;
            }
        }
        if (x - 1 <= boardSize && x - 1 >= 0 && y - 2 <= boardSize && y - 2 >= 0) {
            if (!captured.containsKey(x - 1) && !captured.containsValue(y - 2)) {
                ans++;
            }
        }
        if (x + 1 <= boardSize && x + 1 >= 0 && y - 2 <= boardSize && y - 2 >= 0) {
            if (!captured.containsKey(x + 1) && !captured.containsValue(y - 2)) {
                ans++;
            }
        }
        return ans;
    }

    int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        // possible combinations: (x + 2, j + 1); (x - 2, j + 1); (x + 1, j + 2); (x - 1, j + 2);
        // (x - 2, j - 1); (x + 2, j - 1); (x - 1, j - 2); (x + 1, j - 2)
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();
            if (x + 2 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 2 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x + 1 == x1 && y + 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 1 == x1 && y + 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 2 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x + 2 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 1 == x1 && y - 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x + 1 == x1 && y - 2 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            }
        }
        return ans;
    }
}

// TODO recheck if piece is going to the cell where locates same color piece
class King extends ChessPiece {
    King(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        // possible combinations: (x, j + 1); (x, j - 1); (x + 1, j + 1); (x + 1, j);
        // (x + 1, j - 1); (x - 1, j - 1); (x - 1, j); (x - 1, j + 1)
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        Map<Integer, Integer> captured = new HashMap<>();
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();
            if (x == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
                // possible combinations: (x, j + 1); (x, j - 1); (x + 1, j + 1); (x + 1, j);
                // (x + 1, j - 1); (x - 1, j - 1); (x - 1, j); (x - 1, j + 1)
            } else if (x + 1 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x + 1 == x1 && y == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x + 1 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 1 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 1 == x1 && y == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            } else if (x - 1 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                    captured.put(x1, y1);
                }
            }
        }
        // possible combinations: (x, j + 1); (x, j - 1); (x + 1, j + 1); (x + 1, j);
        // (x + 1, j - 1); (x - 1, j - 1); (x - 1, j); (x - 1, j + 1)
        if (x <= boardSize && x >= 0 && y + 1 <= boardSize && y + 1 >= 0) {
            if (!captured.containsKey(x) && !captured.containsValue(y + 1)) {
                ans++;
            }
        }
        if (x <= boardSize && x >= 0 && y - 1 <= boardSize && y - 1 >= 0) {
            if (!captured.containsKey(x) && !captured.containsValue(y - 1)) {
                ans++;
            }
        }
        if (x + 1 <= boardSize && x + 1 >= 0 && y + 1 <= boardSize && y + 1 >= 0) {
            if (!captured.containsKey(x + 1) && !captured.containsValue(y + 1)) {
                ans++;
            }
        }
        if (x + 1 <= boardSize && x + 1 >= 0 && y <= boardSize && y >= 0) {
            if (!captured.containsKey(x + 1) && !captured.containsValue(y)) {
                ans++;
            }
            // possible combinations: (x, j + 1); (x, j - 1); (x + 1, j + 1); (x + 1, j);
            // (x + 1, j - 1); (x - 1, j - 1); (x - 1, j); (x - 1, j + 1)
        }
        if (x + 1 <= boardSize && x + 1 >= 0 && y - 1 <= boardSize && y - 1 >= 0) {
            if (!captured.containsKey(x + 1) && !captured.containsValue(y - 1)) {
                ans++;
            }
        }
        if (x - 1 <= boardSize && x - 1 >= 0 && y - 1 <= boardSize && y - 1 >= 0) {
            if (!captured.containsKey(x - 1) && !captured.containsValue(y - 1)) {
                ans++;
            }
        }
        if (x - 1 <= boardSize && x - 1 >= 0 && y <= boardSize && y >= 0) {
            if (!captured.containsKey(x - 1) && !captured.containsValue(y)) {
                ans++;
            }
        }
        if (x - 1 <= boardSize && x - 1 >= 0 && y + 1 <= boardSize && y + 1 >= 0) {
            if (!captured.containsKey(x - 1) && !captured.containsValue(y + 1)) {
                ans++;
            }
        }
        return ans;
    }

    int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        // possible combinations: (x, j + 1); (x, j - 1); (x + 1, j + 1); (x + 1, j);
        // (x + 1, j - 1); (x - 1, j - 1); (x - 1, j); (x - 1, j + 1)
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();
            if (x == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
                // possible combinations: (x, j + 1); (x, j - 1); (x + 1, j + 1); (x + 1, j);
                // (x + 1, j - 1); (x - 1, j - 1); (x - 1, j); (x - 1, j + 1)
            } else if (x + 1 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x + 1 == x1 && y == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x + 1 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 1 == x1 && y - 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 1 == x1 && y == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            } else if (x - 1 == x1 && y + 1 == y1) {
                if (piece.getColor() != this.color) {
                    ans++;
                }
            }
        }
        return ans;
    }
}

class Pawn extends ChessPiece {
    Pawn(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    @Override
    int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        // possible movements. 1) white: (x + 1, y), capture: (x + 1, y + 1), (x + 1, y - 1)
        // black: (x - 1, y), capture: (x - 1, y - 1), (x - 1, y + 1)
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        Map<Integer, Integer> captured = new HashMap<>();
        Map<Integer, Integer> same_color_piece = new HashMap<>();
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();
            if (this.color == PieceColor.WHITE) {
                if (x + 1 == x1 && y + 1 == y1) {
                    if (piece.getColor() == PieceColor.BLACK) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                } else if (x + 1 == x1 && y - 1 == y1) {
                    if (piece.getColor() == PieceColor.BLACK) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                }
            } else if (this.color == PieceColor.BLACK) {
                if (x - 1 == x1 && y - 1 == y1) {
                    if (piece.getColor() == PieceColor.WHITE) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                } else if (x - 1 == x1 && y + 1 == y1) {
                    if (piece.getColor() == PieceColor.WHITE) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                }
            }
        }
        // possible movements. 1) white: (x + 1, y), capture: (x + 1, y + 1), (x + 1, y - 1)
        // black: (x - 1, y), capture: (x - 1, y - 1), (x - 1, y + 1)
        if (this.color == PieceColor.WHITE) {
            if (x + 1 <= boardSize & x + 1 >= 1 & y <= boardSize & y >= 1
                    & !(captured.containsKey(x + 1) && captured.containsValue(y))
                    & !(same_color_piece.containsKey(x + 1) & same_color_piece.containsValue(y))) {
                ans++;
            }
        } else if (this.color == PieceColor.BLACK) {
            if (x - 1 <= boardSize & x - 1 >= 1 & y <= boardSize & y >= 1
                    & !(captured.containsKey(x - 1) & captured.containsValue(y))
                    & !(same_color_piece.containsKey(x - 1) & same_color_piece.containsValue(y))) {
                ans++;
            }
        }
        return ans;
    }

    @Override
    int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        // possible movements. 1) white: (x + 1, y), capture: (x + 1, y + 1), (x + 1, y - 1)
        // black: (x - 1, y), capture: (x - 1, y - 1), (x - 1, y + 1)
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();
            if (this.color == PieceColor.WHITE) {
                if (x + 1 == x1 && y + 1 == y1) {
                    if (piece.getColor() == PieceColor.BLACK) {
                        ans++;
                    }
                } else if (x + 1 == x1 && y - 1 == y1) {
                    if (piece.getColor() == PieceColor.BLACK) {
                        ans++;
                    }
                }
            } else if (this.color == PieceColor.BLACK) {
                if (x - 1 == x1 && y - 1 == y1) {
                    if (piece.getColor() == PieceColor.WHITE) {
                        ans++;
                    }
                } else if (x - 1 == x1 && y + 1 == y1) {
                    if (piece.getColor() == PieceColor.WHITE) {
                        ans++;
                    }
                }
            }
        }
        return ans;
    }
}

interface BishopMovement {
    int getDiagonalMovesCount(PiecePosition position, PieceColor color, Map<String, ChessPiece> positions, int boardSize);

    int getDiagonalCapturesCount(PiecePosition position, PieceColor color, Map<String, ChessPiece> positions, int boardSize);
}

interface RookMovement {
    int getOrthogonalMovesCount(PiecePosition position, PieceColor color, Map<String, ChessPiece> positions, int boardSize);

    int getOrthogonalCapturesCount(PiecePosition position, PieceColor color, Map<String, ChessPiece> positions, int boardSize);
}

class Bishop extends ChessPiece implements BishopMovement {
    Bishop(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    @Override
    int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return 0;
    }

    @Override
    int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return 0;
    }

    @Override
    public int getDiagonalMovesCount(PiecePosition position, PieceColor color, Map<String, ChessPiece> positions, int boardSize) {
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        Map<Integer, Integer> captured = new HashMap<>();
        Map<Integer, Integer> same_color_piece = new HashMap<>();
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();

            for (int i = 1; i <= boardSize; i++) {
                // (x++, y++)
                if (x + i == x1 & y + i == y1) {
                    if (piece.getColor() != this.color) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                    break;
                    // (x--, y++)
                } else if (x - i == x1 & y + i == y1) {
                    if (piece.getColor() != this.color) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                    break;
                } else if (x - i == x1 & y - i == y1) {
                    // (x--, y--)
                    if (piece.getColor() != this.color) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                    break;
                } else if (x + i == x1 & y - i == y1) {
                    // (x++, y--)
                    if (piece.getColor() != this.color) {
                        ans++;
                        captured.put(x1, y1);
                    } else {
                        same_color_piece.put(x1, y1);
                    }
                    break;
                }
            }
        }
        // (x++, y++)
        for (int i = 1; i <= boardSize; i++) {
            if (x + i <= boardSize & y + i <= boardSize & !(same_color_piece.containsKey(x + i)
                    & same_color_piece.containsValue(y + i)) & !(captured.containsKey(x + i)
                    & captured.containsValue(y + i))) {
                ans++;
            } else {
                break;
            }
        }
        // (x--, y++)
        for (int i = 1; i <= boardSize; i++) {
            if (x - i >= 1 & y + i <= boardSize & !(same_color_piece.containsKey(x - i)
                    & same_color_piece.containsValue(y + i)) & !(captured.containsKey(x - i)
                    & captured.containsValue(y + i))) {
                ans++;
            } else {
                break;
            }
        }
        // (x--, y--)
        for (int i = 1; i <= boardSize; i++) {
            if (x - i >= 1 & y - i >= 1 & !(same_color_piece.containsKey(x - i)
                    & same_color_piece.containsValue(y - i)) & !(captured.containsKey(x - i)
                    & captured.containsValue(y - i))) {
                ans++;
            } else {
                break;
            }
        }
        // (x++, y--)
        for (int i = 1; i <= boardSize; i++) {
            if (x + i <= boardSize & y - i >= 1 & !(same_color_piece.containsKey(x - i)
                    & same_color_piece.containsValue(y - i)) & !(captured.containsKey(x - i)
                    & captured.containsValue(y - i))) {
                ans++;
            } else {
                break;
            }
        }
        return ans;
    }

    @Override
    public int getDiagonalCapturesCount(PiecePosition position, PieceColor color, Map<String, ChessPiece> positions, int boardSize) {
        int x = position.getX();
        int y = position.getY();
        int ans = 0;
        for (Map.Entry<String, ChessPiece> entry : positions.entrySet()) {
            ChessPiece piece = entry.getValue();
            int x1 = piece.getPosition().getX();
            int y1 = piece.getPosition().getY();

            for (int i = 1; i <= boardSize; i++) {
                // (x++, y++)
                if (x + i == x1 & y + i == y1) {
                    if (piece.getColor() != this.color) {
                        ans++;
                    }
                    break;
                    // (x--, y++)
                } else if (x - i == x1 & y + i == y1) {
                    if (piece.getColor() != this.color) {
                        ans++;
                    }
                    break;
                } else if (x - i == x1 & y - i == y1) {
                    // (x--, y--)
                    if (piece.getColor() != this.color) {
                        ans++;
                    }
                    break;
                } else if (x + i == x1 & y - i == y1) {
                    // (x++, y--)
                    if (piece.getColor() != this.color) {
                        ans++;
                    }
                    break;
                }
            }
        }
        return ans;
    }
}


class Rook extends ChessPiece {
    Rook(PiecePosition position, PieceColor color) {
        super(position, color);
    }
}

class Queen extends ChessPiece {
    Queen(PiecePosition position, PieceColor color) {
        super(position, color);
    }
}