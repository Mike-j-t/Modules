package mjt.emsg;

/**
 * Emsg - Class for  and extended error messaging object
 */
public class Emsg {
    private boolean error_indicator;
    private int error_number;
    private String error_message;

    @SuppressWarnings("unused")
    public Emsg() {
        this.error_indicator = true;
        this.error_number = 0;
        this.error_message = "";
    }

    @SuppressWarnings("unused")
    public Emsg(@SuppressWarnings("SameParameterValue")
                         boolean error_indicator,
                 @SuppressWarnings("SameParameterValue")
                         int error_number,
                 @SuppressWarnings("SameParameterValue")
                         String error_message) {
        this.error_indicator = error_indicator;
        this.error_number = error_number;
        this.error_message = error_message;
    }

    /**
     * get wether or not an error has been set
     * @return      true if an error else false
     */
    @SuppressWarnings("unused")
    public boolean getErrorIndicator() {
        return this.error_indicator;
    }

    /**
     * get the error number
     * @return  the error number
     */
    @SuppressWarnings("unused")
    public int getErrorNumber() {
        return this.error_number;
    }

    /**
     * get the message associated with the error
     * @return  the message associated with the error
     */
    public String getErrorMessage() {
        return  this.error_message;
    }

    /**
     * set the error indicator
     * @param error_indicator   true to indicate an error else false
     */
    @SuppressWarnings("unused")
    public void setErrorIndicator(boolean error_indicator) {
        this.error_indicator = error_indicator;
    }

    /**
     * set the error number
     * @param error_number  error number
     */
    @SuppressWarnings("unused")
    public void  setErrorNumber(int error_number) {
        this.error_number = error_number;
    }

    /**
     * set the error associated with the message
     * @param error_message     error message
     */
    @SuppressWarnings("unused")
    public void setErrorMessage(String error_message) {
        this.error_message = error_message;
    }

    /**
     * Set all values for the error
     * @param error_indicator   true to indicate an error, else false
     * @param error_number      number to associate with the error
     * @param error_message     message to associate with the error
     */
    public void setAll(@SuppressWarnings("SameParameterValue") boolean error_indicator, int error_number, String error_message) {
        this.error_indicator = error_indicator;
        this.error_number = error_number;
        this.error_message = error_message;
    }
}
