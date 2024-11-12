package hyundai.movie_review.thear_down.dto;

public record ThearDownResponse(
        String message
) {
    //정적 팩토리 메서드로 dto에서 직접 만들 수 있도록 함
    public static ThearDownResponse of(String message){
        return new ThearDownResponse(message);
    }
}