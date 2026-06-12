package chess;


import java.util.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor turn;
    final  HashSet<ChessPosition> blackPieces;
    final  HashSet<ChessPosition> whitePieces;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;
    private boolean gameOver = false;


    public ChessGame() {
        turn = TeamColor.WHITE;
        blackPieces = new HashSet<>();
        whitePieces = new HashSet<>();
        ChessBoard newBoard = new ChessBoard();
        newBoard.resetBoard();
        setBoard(newBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(gameOver){
            return;
        }
        turn = team;
        if (team == TeamColor.BLACK){
            board.setWhiteEnPasant(new ChessPosition(-1,-1));
        }
        else {
            board.setBlackEnPasant(new ChessPosition(-1,-1));
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }


    private HashMap<ChessPosition,HashSet<ChessPosition>> getThreatened(ChessBoard board, TeamColor opponent, boolean getTheoretical){
        updatePieces(board);
        HashMap<ChessPosition,HashSet<ChessPosition>> threats = new HashMap<>();
        HashSet<ChessPosition> enemies = (opponent == TeamColor.WHITE) ? whitePieces : blackPieces;

        for (ChessPosition pos : enemies){
            ChessPiece piece = board.getPiece(pos);
            var threatened = piece.pieceMoves(board,pos);
            if(getTheoretical){
                threatened = piece.theoreticalMoves(board,pos);
            }
            for(var threat : threatened){
                if(threats.containsKey(threat.getEndPosition())){
                    threats.get(threat.getEndPosition()).add(pos);
                }
                else{
                    threats.put(threat.getEndPosition(),new HashSet<>());
                    threats.get(threat.getEndPosition()).add(pos);
                }
            }
        }
        return threats;
    }

    private boolean validMove(ChessBoard board,HashMap<ChessPosition,HashSet<ChessPosition>> theoretical, TeamColor player,ChessPosition mustProtect){
        HashSet<ChessPosition> allEnemyPieces;
        if(player == TeamColor.BLACK){
            allEnemyPieces = whitePieces;
        }
        else {
            allEnemyPieces = blackPieces;
        }
        if(!theoretical.containsKey(mustProtect)){
            return true;
        }
        HashMap<ChessPosition,HashSet<ChessPosition>> newThreats = new HashMap<>();
        for (var pos : allEnemyPieces){
            ChessPiece piece = board.getPiece(pos);

            var threatened = piece.pieceMoves(board,pos);
            for(var threat : threatened){
                if(newThreats.containsKey(threat.getEndPosition())){
                    newThreats.get(threat.getEndPosition()).add(pos);
                }
                else{
                    newThreats.put(threat.getEndPosition(),new HashSet<>());
                    newThreats.get(threat.getEndPosition()).add(pos);
                }
            }
        }
        return !(newThreats.containsKey(mustProtect));
    }

    private ChessBoard makeBoardFromMove(ChessMove move){

        ChessBoard copy;
        copy = board.clone();

        copy.setBlackEnPasant(board.getBlackEnPasant());
        copy.setWhiteEnPasant(board.getWhiteEnPasant());

        ChessPiece piece = copy.getPiece(move.getStartPosition());
        if(piece == null){
            return copy;
        }
        if (move.getPromotionPiece() == null){
            copy.setPiece(move.getEndPosition(),piece);
            copy.setPiece(move.getStartPosition(),null);
        }
        else {
            copy.setPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),move.getPromotionPiece()));
            copy.setPiece(move.getStartPosition(),null);
        }
        return copy;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        updatePieces(board);
        ChessPiece piece = board.getPiece(startPosition);
        if(piece == null){
            return null;
        }
        Collection<ChessMove> possible = piece.pieceMoves(board,startPosition);
        Collection<ChessMove> actual = new ArrayList<>();


        TeamColor opponent = (piece.getTeamColor() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        HashMap<ChessPosition,HashSet<ChessPosition>> theoretical = getThreatened(board,opponent,true);
        HashMap<ChessPosition,HashSet<ChessPosition>> actualThreats = getThreatened(board,opponent,false);



        for(var move : possible){
            boolean isValid;
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                isValid = validMove(makeBoardFromMove(move),theoretical,piece.getTeamColor(),move.getEndPosition());
            }else {
                ChessPosition mustDefend = (piece.getTeamColor() == TeamColor.WHITE) ? whiteKing: blackKing;
                isValid = validMove(makeBoardFromMove(move),theoretical,piece.getTeamColor(),mustDefend);
            }
            if(isValid){
                if (startPosition == ((piece.getTeamColor() == TeamColor.WHITE) ? whiteKing: blackKing)){
                    if (actualThreats.containsKey(move.getEndPosition())){
                        continue;
                    }
                }
                actual.add(move);
            }
        }
        return actual;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(gameOver){
            return;
        }
        ChessBoard prev = new ChessBoard();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++){
                prev.addPiece(new ChessPosition(i+1,j+1),board.getPiece(new ChessPosition(i+1,j+1)));
            }
        }
        prev.setBlackEnPasant(board.getBlackEnPasant());
        prev.setWhiteEnPasant(board.getWhiteEnPasant());

        ChessPosition start = move.getStartPosition();
        ChessPiece piece = board.getPiece(start);

        Collection<ChessMove> valid = validMoves(start);


        if(piece == null){
            throw new InvalidMoveException(String.format("There is no piece at %s",start));
        }

        if (piece.getTeamColor() != turn){
            throw new InvalidMoveException(String.format("It is not %s's turn",turn));
        }

        boolean isValid = false;
        for(var thing : valid){
            if (thing.equals(move)){
                isValid = true;
                break;
            }
        }
        if(!isValid){
            throw new InvalidMoveException(String.format("%s is not a valid move: bad move",move));
        }
        if (move.getPromotionPiece() == null){
            board.setPiece(move.getEndPosition(),piece);
            board.setPiece(move.getStartPosition(),null);
        }
        else {
            board.setPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),move.getPromotionPiece()));
            board.setPiece(move.getStartPosition(),null);
        }

//        if(isInCheck(turn)){
//            board = prev;
//            throw new InvalidMoveException(String.format("%s is not a valid move: still in check",move));
//        }
        updatePieces(board);
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            if(board.getBlackEnPasant().equals( move.getEndPosition())){
                board.setPiece(new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn()), null);
            }
            else if (board.getWhiteEnPasant().equals( move.getEndPosition())){
                board.setPiece(new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn()), null);
            }

            if(move.getStartPosition().getRow() - move.getEndPosition().getRow() < -1){
                board.setBlackEnPasant(new ChessPosition(move.getStartPosition().getRow() + 1,move.getStartPosition().getColumn()));
            }
            else if(move.getStartPosition().getRow() - move.getEndPosition().getRow() > 1){
                board.setWhiteEnPasant(new ChessPosition(move.getStartPosition().getRow() - 1,move.getStartPosition().getColumn()));
            }
        }
        setTeamTurn((turn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        var temp = (teamColor == TeamColor.WHITE) ? blackPieces : whitePieces;
        var enemies = new HashSet<>(temp);

        HashSet<ChessPosition> actual = new HashSet<>();

        for(var enemy : enemies){
            var threatened = board.getPiece(enemy).pieceMoves(board,enemy);
            for (var threat : threatened){
                actual.add(threat.getEndPosition());
            }
        }

        if(teamColor == TeamColor.BLACK){
            return actual.contains(blackKing);
        }
        else {
            return actual.contains(whiteKing);
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck( teamColor)){
            return false;
        }
        var temp =(teamColor == TeamColor.WHITE) ? whitePieces : blackPieces;
        var pieces = new HashSet<>(temp);
        if( teamColor == TeamColor.BLACK){
            for(var pos : pieces){
                Collection<ChessMove> possible = validMoves(pos);
                if(possible.isEmpty()){
                    continue;
                }
                return false;
            }
        }
        else{
            for(var pos : pieces){
                Collection<ChessMove> possible = validMoves(pos);
                if(possible.isEmpty()){
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        }

        HashSet<ChessPosition> original = (teamColor == TeamColor.BLACK) ? blackPieces : whitePieces;
        ArrayList<ChessPosition> pieces = new ArrayList<>(original);

        for(var pos : pieces){
            Collection<ChessMove> possible = validMoves(pos);
            if(possible.isEmpty()){
                continue;
            }
            return false;
        }

        return true;
    }

    private void updatePieces(ChessBoard board){
        blackPieces.clear();
        whitePieces.clear();

        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPiece at = board.getPiece(new ChessPosition(i+1,j+1));
                if (at == null){
                    continue;
                }
                if (at.getTeamColor() == TeamColor.WHITE){
                    whitePieces.add(new ChessPosition(i+1,j+1));
                    if(at.getPieceType() == ChessPiece.PieceType.KING){
                        whiteKing = new ChessPosition(i+1,j+1);
                    }
                }
                else{
                    blackPieces.add(new ChessPosition(i+1,j+1));
                    if(at.getPieceType() == ChessPiece.PieceType.KING){
                        blackKing = new ChessPosition(i+1,j+1);
                    }
                }
            }
        }
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        updatePieces(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public void setGameOver(boolean val){
        gameOver = val;
    }

    public boolean getGameOver(){
        return gameOver;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", turn=" + turn +
                ", gameOver=" + gameOver +
                ", \nblackKing=" + blackKing +
                ", whiteKing=" + whiteKing +
                ", \nwhitePieces=" + whitePieces +
                ", \nblackPieces=" + blackPieces +
                ", \nThreatened by white=" + getThreatened(board,TeamColor.WHITE,false) +
                ", \nThreatened by black=" + getThreatened(board,TeamColor.BLACK,false) +
                '}';
    }
}
