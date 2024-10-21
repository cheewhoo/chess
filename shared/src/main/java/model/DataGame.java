package model;
import chess.ChessGame;

public record DataGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}