package cleancode.studycafe.tobe.model;

public class StudyCafeSeatPass implements StudyCafePass{

    private final StudyCafePassType passType;
    private final int duration;
    private final int price;
    private final double discountRate;

    private StudyCafeSeatPass(StudyCafePassType passType, int duration, int price, double discountRate) {
        this.passType = passType;
        this.duration = duration;
        this.price = price;
        this.discountRate = discountRate;
    }

    public static StudyCafeSeatPass of(StudyCafePassType passType, int duration, int price, double discountRate) {
        return new StudyCafeSeatPass(passType, duration, price, discountRate);
    }
    @Override
    public StudyCafePassType getPassType(){
        return passType;
    }

    @Override
    public int getDuration() {
        return duration;
    }
    @Override
    public int getPrice() {
        return price;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public boolean hasSamePassType(StudyCafePassType studyCafePassType){
        return passType == studyCafePassType;
    }

    public boolean hasSamePassTypeAndDuration(StudyCafePassType passType, int duration) {
        return this.passType == passType
                && this.duration == duration;
    }
    public int calculateDiscountPrice(){
        return (int) (price * discountRate);
    }
    public int calculateTotalPrice(StudyCafeLockerPass lockerPass){
        return price - calculateDiscountPrice() + (lockerPass != null ? lockerPass.getPrice() : 0);
    }
}
