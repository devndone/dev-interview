package after.acco.paypal.solution;

public class AmortizationScheduleConstant {

	private static final double[] borrowAmountRange = new double[] { 0.01d, 1000000000000d };
	private static final double[] aprRange = new double[] { 0.000001d, 100d };
	private static final int[] termRange = new int[] { 1, 1000000 };

	
	public static final double[] getBorrowAmountRange() {
		return borrowAmountRange.clone();
	}
	
	public static final double[] getAPRRange() {
		return aprRange.clone();
	}

	public static final int[] getTermRange() {
		return termRange.clone();
	}
}
