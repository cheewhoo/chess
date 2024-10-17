package model;
import chess.ChessGame;

public record Data_Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}