package after.acco.paypal.solution;

//Exercise Details:
//Build an amortization schedule program using Java. 
// 
//The program should prompt the user for
//		the amount he or she is borrowing,
//		the annual percentage rate used to repay the loan,
//		the term, in years, over which the loan is repaid.  
// 
//The output should include:
//		The first column identifies the payment number.
//		The second column contains the amount of the payment.
//		The third column shows the amount paid to interest.
//		The fourth column has the current balance.  The total payment amount and the interest paid fields.
// 
//Use appropriate variable names and comments.  You choose how to display the output (i.e. Web, console).  
//Amortization Formula
//This will get you your monthly payment.  Will need to update to Java.
//M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
// 
//Where:
//P = Principal
//I = Interest
//J = Monthly Interest in decimal form:  I / (12 * 100)
//N = Number of months of loan
//M = Monthly Payment Amount
// 
//To create the amortization table, create a loop in your program and follow these steps:
//1.      Calculate H = P x J, this is your current monthly interest
//2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
//3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
//4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
// 

import java.io.Console;
import java.io.IOException;
import after.acco.paypal.solution.AmortizationSchedule;

public class AmortizationScheduleOrganizer {

	private static final Console console = System.console();
	
	private boolean inputReady = false;
	private AmortizationSchedule amortizationSchedule;

	public static void main(String[] args) throws InvalidStateException {
		AmortizationScheduleOrganizer as = new AmortizationScheduleOrganizer();
		try {
			as.getInputAndValidate();
		} catch (IllegalArgumentException e) {
			AmortizationScheduleUtil.print(console, "Unable to process the values entered. Terminating program.\n");
		}
		if(as.isInputReady()) {
			as.outputAmortizationSchedule();
		} else {
			AmortizationScheduleUtil.print(console, "Unable to process the values entered. Terminating program.\n");
		}
	}
	
	public boolean getInputAndValidate() throws InvalidStateException {
		AmortizationScheduleValidator av = new AmortizationScheduleValidator();
		AmortizationSchedule as = new AmortizationSchedule();
		boolean res = true;
		String[] userPrompts = {
				"Please enter the amount you would like to borrow: ",
				"Please enter the annual percentage rate used to repay the loan: ",
				"Please enter the term, in years, over which the loan is repaid: "
		};
		String line = "";
		String errorMessage = "Invalid Input!";
		for (int i = 0; i< userPrompts.length; ) {
			String userPrompt = userPrompts[i];
			try {
				line = AmortizationScheduleUtil.readLine(console, userPrompt);
			} catch (IOException e) {
				AmortizationScheduleUtil.print(console, "An IOException was encountered. Terminating program.\n");
			}
			if (av.validateInputAndSet(i, line, errorMessage, as)) {
				i++;
			} else {
				res = false;
				AmortizationScheduleUtil.print(console, errorMessage);
				break;
			}
		}
		if(res) {
			as.validateState();
			this.amortizationSchedule = as;
		}
		return res;
	}
	
	// The output should include:
	//	The first column identifies the payment number.
	//	The second column contains the amount of the payment.
	//	The third column shows the amount paid to interest.
	//	The fourth column has the current balance.  The total payment amount and the interest paid fields.
	public void outputAmortizationSchedule() throws InvalidStateException {
		if(this.isInputReady() && this.amortizationSchedule != null && this.amortizationSchedule.isValidState()) {
			// 
			// To create the amortization table, create a loop in your program and follow these steps:
			// 1.      Calculate H = P x J, this is your current monthly interest
			// 2.      Calculate C = M - H, this is your monthly payment minus your monthly interest, so it is the amount of principal you pay for that month
			// 3.      Calculate Q = P - C, this is the new balance of your principal of your loan.
			// 4.      Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
			// 
	
			String formatString = "%1$-20s%2$-20s%3$-20s%4$s,%5$s,%6$s\n";
			AmortizationScheduleUtil.printf(console, formatString,
					"PaymentNumber", "PaymentAmount", "PaymentInterest",
					"CurrentBalance", "TotalPayments", "TotalInterestPaid");
			double amountBorrowed = this.amortizationSchedule.getAmountBorrowed();
			double balance = amountBorrowed;
			int paymentNumber = 0;
			long totalPayments = 0;
			long totalInterestPaid = 0;
			
			// output is in dollars
			formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$.2f,%5$.2f,%6$.2f\n";
			AmortizationScheduleUtil.printf(console, formatString, paymentNumber++, 0d, 0d,
					((double) amountBorrowed) / 100d,
					((double) totalPayments) / 100d,
					((double) totalInterestPaid) / 100d);
			
			final int maxNumberOfPayments = this.amortizationSchedule.getInitialTermMonths() + 1;
			while ((balance > 0) && (paymentNumber <= maxNumberOfPayments)) {
				// Calculate H = P x J, this is your current monthly interest
				long curMonthlyInterest = Math.round(((double) balance) * this.amortizationSchedule.getMonthlyInterest());
	
				// the amount required to payoff the loan
				double curPayoffAmount = balance + curMonthlyInterest;
				
				// the amount to payoff the remaining balance may be less than the calculated monthlyPaymentAmount
				double curMonthlyPaymentAmount = Math.min(this.amortizationSchedule.getMonthlyPaymenAmountt(), curPayoffAmount);
				
				// it's possible that the calculated monthlyPaymentAmount is 0,
				// or the monthly payment only covers the interest payment - i.e. no principal
				// so the last payment needs to payoff the loan
				if ((paymentNumber == maxNumberOfPayments) &&
						((curMonthlyPaymentAmount == 0) || (curMonthlyPaymentAmount == curMonthlyInterest))) {
					curMonthlyPaymentAmount = curPayoffAmount;
				}
				
				// Calculate C = M - H, this is your monthly payment minus your monthly interest,
				// so it is the amount of principal you pay for that month
				double curMonthlyPrincipalPaid = curMonthlyPaymentAmount - curMonthlyInterest;
				
				// Calculate Q = P - C, this is the new balance of your principal of your loan.
				double curBalance = balance - curMonthlyPrincipalPaid;
				
				totalPayments += curMonthlyPaymentAmount;
				totalInterestPaid += curMonthlyInterest;
				
				// output is in dollars
				AmortizationScheduleUtil.printf(console, formatString, paymentNumber++,
						((double) curMonthlyPaymentAmount) / 100d,
						((double) curMonthlyInterest) / 100d,
						((double) curBalance) / 100d,
						((double) totalPayments) / 100d,
						((double) totalInterestPaid) / 100d);
							
				// Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
				balance = curBalance;
			}
		} else {
			throw new InvalidStateException("Amortization ScheduleOrganizer is in Invalid State, Please try later!");
		}
	}

	public boolean isInputReady() {
		return inputReady;
	}

	public void setInputReady(boolean inputReady) {
		this.inputReady = inputReady;
	}
	
	/*public AmortizationSchedule getAmortizationSchedule() {
		return amortizationSchedule;
	}

	public void setAmortizationSchedule(final AmortizationSchedule amortizationSchedule) {
		this.amortizationSchedule = amortizationSchedule;
	}*/
		
}
