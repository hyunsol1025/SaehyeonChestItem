package saehyeon.chestitem;

public enum ErrorType {
    NOT_EXIST_REGION("지역이 등록되어 있지 않습니다."),
    NOT_ITEMS("지역에 등록된 아이템이 없습니다."),
    NOT_CHEST_ENOUGH("등록된 아이템을 숨길 만큼 상자가 충분치 않습니다."),
    INVALID_POSITION("범위가 제대로 지정되지 않았습니다.");

    String errorMessage;

    ErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toMessage() {
        return "§c"+errorMessage;
    }
}
