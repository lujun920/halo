package org.xujin.halo.test.customer.interceptor;

import org.xujin.halo.command.CommandInterceptorI;
import org.xujin.halo.command.PreInterceptor;
import org.xujin.halo.dto.Command;
import org.xujin.halo.exception.ParamException;
import org.xujin.halo.validator.HaloMessageInterpolator;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * ValidationInterceptor
 *
 * @author xujin 2018-01-06 8:27 PM
 */
@PreInterceptor
public class ValidationInterceptor implements CommandInterceptorI {

    //Enable fail fast, which will improve performance
    private ValidatorFactory factory = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
            .messageInterpolator(new HaloMessageInterpolator()).buildValidatorFactory();

    @Override
    public void preIntercept(Command command) {
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Command>> constraintViolations = validator.validate(command);
        constraintViolations.forEach(violation -> {
            throw new ParamException(violation.getPropertyPath() + " " + violation.getMessage());
        });
    }
}