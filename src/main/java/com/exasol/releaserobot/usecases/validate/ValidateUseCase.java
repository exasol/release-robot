package com.exasol.releaserobot.usecases.validate;

import com.exasol.releaserobot.usecases.Report;
import com.exasol.releaserobot.usecases.UserInput;

/**
 * Interface for the Validate use case.
 */
public interface ValidateUseCase {
    /**
     * Validate project.
     *
     * @param userInput user input
     * @return new {@link Report}
     */
    public Report validate(final UserInput userInput);
}