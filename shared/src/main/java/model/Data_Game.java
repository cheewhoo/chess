package model;
import chess.ChessGame;

public record Data_Game(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game) {}