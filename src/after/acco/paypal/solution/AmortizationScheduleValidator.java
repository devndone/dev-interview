package after.acco.paypal.solution;

public class AmortizationScheduleValidator {
	
	public boolean isValidBorrowAmount(double amount) {
		double range[] = AmortizationScheduleConstant.getBorrowAmountRange();
		return ((range[0] <= amount) && (amount <= range[1]));
	}
	
	public boolean isValidAPRValue(double rate) {
		double range[] = AmortizationScheduleConstant.getAPRRange();
		return ((range[0] <= rate) && (rate <= range[1]));
	}
	
	public boolean isValidTerm(int years) {
		int range[] = AmortizationScheduleConstant.getTermRange();
		return ((range[0] <= years) && (years <= range[1]));
	}
	
	public boolean validateInputAndSet(int uerPrompt, final String line, String errorMessage, AmortizationSchedule amortizedSchedule) {
		boolean isValidValue = true;
		double amount = 0;
		double apr = 0;
		int years = 0;
		try {
			switch (uerPrompt) {
			case 0:
				amount = Double.parseDouble(line);
				if (isValidBorrowAmount(amount) == false) {
					isValidValue = false;
					double range[] = AmortizationScheduleConstant.getBorrowAmountRange();
					errorMessage = "Please enter a positive value between " + range[0] + " and " + range[1] + ". ";
				} else {
					amortizedSchedule.setAmountBorrowed(amount);
				}
				break;
			case 1:
				apr = Double.parseDouble(line);
				if (isValidAPRValue(apr) == false) {
					isValidValue = false;
					double range[] = AmortizationScheduleConstant.getAPRRange();
					errorMessage = "Please enter a positive value between " + range[0] + " and " + range[1] + ". ";
				} else {
					amortizedSchedule.setApr(apr);
				}
				break;
			case 2:
				years = Integer.parseInt(line);
				if (isValidTerm(years) == false) {
					isValidValue = false;
					int range[] = AmortizationScheduleConstant.getTermRange();
					errorMessage = "Please enter a positive integer value between " + range[0] + " and " + range[1] + ". ";
				} else {
					amortizedSchedule.setInitialTermMonths(years);
				}
				break;
			}
		} catch (NumberFormatException e) {
			isValidValue = false;
		}
		return isValidValue;
	}
}
