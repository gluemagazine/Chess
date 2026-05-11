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
    final private HashSet<ChessPosition> blackPieces;
    final private HashSet<ChessPosition> whitePieces;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;



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
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public HashMap<ChessPosition,HashSet<ChessPosition>> getThreatened(ChessBoard board, TeamColor opponent, boolean getTheoretical){
        updatePieces(board);

        HashMap<ChessPosition,HashSet<ChessPosition>> threats = new HashMap<>();
        if(opponent == TeamColor.WHITE){
            for (ChessPosition pos : whitePieces){
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
        }
        else {
            for (ChessPosition pos : blackPieces){
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
        }
        return threats;
    }

    public boolean validMove(ChessBoard board,HashMap<ChessPosition,HashSet<ChessPosition>> theoretical, TeamColor player,ChessPosition mustProtect){
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

    public ChessBoard makeBoardFromMove(ChessMove move){

        ChessBoard copy = new ChessBoard();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++){
                copy.addPiece(new ChessPosition(i+1,j+1),board.getPiece(new ChessPosition(i+1,j+1)));
            }
        }


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
        System.out.println("Starting to get moves");
        System.out.println(board);
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
                isValid = validMove(makeBoardFromMove(move),theoretical,piece.getTeamColor(),(piece.getTeamColor() == TeamColor.WHITE) ? whiteKing: blackKing);
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
        ChessBoard prev = new ChessBoard();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++){
                prev.addPiece(new ChessPosition(i+1,j+1),board.getPiece(new ChessPosition(i+1,j+1)));
            }
        }
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
            throw new InvalidMoveException(String.format("%s is not a valid move",move));
        }
        if (move.getPromotionPiece() == null){
            board.setPiece(move.getEndPosition(),piece);
            board.setPiece(move.getStartPosition(),null);
        }
        else {
            board.setPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(),move.getPromotionPiece()));
            board.setPiece(move.getStartPosition(),null);
        }

        if(isInCheck(turn)){
            board = prev;
            throw new InvalidMoveException(String.format("%s is not a valid move",move));
        }
        updatePieces(board);
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

        if( teamColor == TeamColor.BLACK){
            for(var pos : blackPieces){
                Collection<ChessMove> possible = validMoves(pos);
                if(possible.isEmpty()){
                    continue;
                }
                return false;
            }
        }
        else{
            for(var pos : whitePieces){
                Collection<ChessMove> possible = validMoves(pos);
                if(possible.isEmpty()){
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public void updatePieces(ChessBoard board){
        blackPieces.clear();
        whitePieces.clear();
        ChessBoard copy = new ChessBoard();
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++){
                copy.addPiece(new ChessPosition(i+1,j+1),board.getPiece(new ChessPosition(i+1,j+1)));
            }
        }
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
        if (!copy.equals(board)){
            System.out.println("There was a problem");
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
                '}';
    }
}
