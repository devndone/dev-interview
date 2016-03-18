package after.acco.paypal.solution;

public class AmortizationSchedule {

	private final double monthlyInterestDivisor = 12d * 100d;
	
	private double amountBorrowed = 0d;		// in cents
	private double apr = 0d;
	private int initialTermMonths = 0;
	private long monthlyPaymentAmount = 0;	// in cents
	private double monthlyInterest = 0d;
	private boolean validState;
	
	public AmortizationSchedule(double amount, double interestRate, int years) {
		amountBorrowed = Math.round(amount * 100);
		apr = interestRate;
		initialTermMonths = years * 12;
		this.calculateMonthlyPayment();
	}
	
	public AmortizationSchedule() {
		// TODO Auto-generated constructor stub
	}
	
	public void validateState() throws InvalidStateException {
		if(!this.validState) {
			// the following shouldn't happen with the available valid ranges
			// for borrow amount, apr, and term; however, without range validation,
			// monthlyPaymentAmount as calculated by calculateMonthlyPayment()
			// may yield incorrect values with extreme input values
			this.calculateMonthlyPayment();
			if (this.monthlyPaymentAmount > this.amountBorrowed) {
				throw new InvalidStateException("Amortization Schedule is in Invalid State, Please try later!");
			}
			this.validState = true;
		}
	}

	private void calculateMonthlyPayment() {
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		//
		// Where:
		// P = Principal
		// I = Interest
		// J = Monthly Interest in decimal form:  I / (12 * 100)
		// N = Number of months of loan
		// M = Monthly Payment Amount
		// 
		
		// calculate J
		this.monthlyInterest = apr / monthlyInterestDivisor;
		
		// this is 1 / (1 + J)
		double tmp = Math.pow(1d + monthlyInterest, -1);
		
		// this is Math.pow(1/(1 + J), N)
		tmp = Math.pow(tmp, initialTermMonths);
		
		// this is 1 / (1 - (Math.pow(1/(1 + J), N))))
		tmp = Math.pow(1d - tmp, -1);
		
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		double rc = amountBorrowed * monthlyInterest * tmp;
		
		this.monthlyPaymentAmount = Math.round(rc);
	}
	
	public double getAmountBorrowed() {
		return amountBorrowed;
	}

	public void setAmountBorrowed(double amountBorrowed) {
		this.amountBorrowed = amountBorrowed;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public int getInitialTermMonths() {
		return initialTermMonths;
	}

	public void setInitialTermMonths(int initialTermMonths) {
		this.initialTermMonths = initialTermMonths;
	}

	public boolean isValidState() {
		return this.validState;
	}
	
	public double getMonthlyInterest() {
		return this.monthlyInterest;
	}

	public double getMonthlyPaymenAmountt() {
		return this.monthlyPaymentAmount;
	}
}
