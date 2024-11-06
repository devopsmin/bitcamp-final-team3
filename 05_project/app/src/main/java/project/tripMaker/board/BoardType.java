package project.tripMaker.board;

public enum BoardType {
    QUESTION(1), COMPANION(2), REVIEW(3);

    private final int typeCode;

    BoardType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public static BoardType fromTypeCode(int typeCode) {
        for (BoardType type : BoardType.values()) {
            if (type.getTypeCode() == typeCode) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BoardType code: " + typeCode);
    }
}
