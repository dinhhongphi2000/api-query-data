package org.apiquery.shared.utils;

import org.apiquery.dtos.ResponseStatusDTO;
import org.apiquery.shared.enums.ResponseStatusCode;
import org.apiquery.shared.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdvanceController {
    protected String getViewFolderLocationBaseOnController() {

        return "";
    }

    protected String getViewName() {
        return getCallingMethod();
    }

    protected ModelAndView view() {
        String view = getViewFolderLocationBaseOnController() + "/" + getViewName();
        return new ModelAndView(view);
    }

    protected ModelAndView view(String viewName) {
        String view = getViewFolderLocationBaseOnController() + "/" + viewName;
        return new ModelAndView(view);
    }

    protected ModelAndView view(String viewName, @Nullable Map<String, ?> model) {
        String view = getViewFolderLocationBaseOnController() + "/" + viewName;
        return new ModelAndView(view, model);
    }

    protected ModelAndView view(@Nullable Map<String, ?> model) {
        String view = getViewFolderLocationBaseOnController() + "/" + getViewName();
        return new ModelAndView(view, model);
    }

    protected ModelAndView view(String viewName, HttpStatus status) {
        String view = getViewFolderLocationBaseOnController() + "/" + viewName;
        return new ModelAndView(view, status);
    }

    protected ModelAndView view(HttpStatus status) {
        String view = getViewFolderLocationBaseOnController() + "/" + getViewName();
        return new ModelAndView(view, status);
    }

    protected ModelAndView view(@Nullable String viewName, @Nullable Map<String, ?> model,
            @Nullable HttpStatus status) {
        String view = getViewFolderLocationBaseOnController() + "/" + viewName;
        return new ModelAndView(view, model, status);
    }

    protected ModelAndView view(@Nullable Map<String, ?> model, @Nullable HttpStatus status) {
        String view = getViewFolderLocationBaseOnController() + "/" + getViewName();
        return new ModelAndView(view, model, status);
    }

    protected ModelAndView view(String viewName, String modelName, Object modelObject) {
        String view = getViewFolderLocationBaseOnController() + "/" + viewName;
        return new ModelAndView(view, modelName, modelObject);
    }

    protected ModelAndView view(String modelName, Object modelObject) {
        String view = getViewFolderLocationBaseOnController() + "/" + getViewName();
        return new ModelAndView(view, modelName, modelObject);
    }

    protected ModelAndView forward(@Nullable String viewName, String modelName, Object modelObject) {
        String view = getViewFolderLocationBaseOnController() + "/" + viewName;
        return new ModelAndView("forward:/" + view, modelName, modelObject);
    }

    protected ModelAndView redirectToAction(String path) {
        if (path.startsWith("/")) {
            path = path.replaceFirst("/", "");
        }
        return new ModelAndView("redirect:/" + path);
    }

    protected ModelAndView redirectToPath(String path) {
        return new ModelAndView("redirect:" + path);
    }

    protected String getCallingMethod() {
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    // protected void setPagingForView(PageData pageData, HttpServletRequest
    // request) {
    // request.setAttribute("totalPage", pageData.getTotalPages());
    // request.setAttribute("currentPage", pageData.getCurrentPage());
    // request.setAttribute("maxItemPerPage", pageData.getMaxItemPerPage());
    // }

    protected void showBindingResultError(BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        String errorMessage = "Có lỗi xảy ra";
        if (bindingResult.getFieldErrors().size() > 0) {
            errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            httpServletRequest.setAttribute("errors", errorMessage);
        }
    }

    protected ResponseEntity<ResponseStatusDTO> success(Object object) {
        ResponseStatusDTO response = new ResponseStatusDTO();
        response.setStatus(ResponseStatusCode.SUCCESS.getName());
        response.setResults(object);
        return json(response, HttpStatus.OK);
    }

    protected ResponseEntity<ResponseStatusDTO> fail(String message) {
        ResponseStatusDTO response = new ResponseStatusDTO();
        response.setStatus(ResponseStatusCode.ERROR.getName());
        response.setMessage(message);
        return json(response, HttpStatus.OK);
    }


    protected ResponseEntity<ResponseStatusDTO> fail(Exception ex, HttpServletRequest httpRequest) {
        String message;
        if (ex instanceof ServiceException) {
            // logger.error(ex.getMessage(), ex);
            // ex.printStackTrace();
            // String code = ((ServiceException)ex).getCode();
            // Object[] data = ((ServiceException) ex).getData();
            // if(code.isEmpty()) {
            // message = "Không xác định được lỗi";
            // }else {
            // message = errorMessageSource.getMessage(code, data,
            // RequestContextUtils.getLocale(httpRequest));
            // }
            message = ex.getMessage();
        } else {
            message = ex.getMessage();
        }

        ResponseStatusDTO response = new ResponseStatusDTO();
        response.setStatus(ResponseStatusCode.ERROR.getName());
        response.setMessage(message);
        return json(response, HttpStatus.OK);

    }

    protected void setErrorMessage(Exception ex, HttpServletRequest httpRequest) {
        String message;
        if (ex instanceof ServiceException) {
            String code = ((ServiceException) ex).getCode();
            if (code.isEmpty()) {
                message = "Không xác định được lỗi";
            } else {
                message = "Không xác định được lỗi";
                // message = errorMessageSource.getMessage(code, null, RequestContextUtils.getLocale(httpRequest));
            }
        } else {
            message = "Không xác định được lỗi";
        }
        httpRequest.setAttribute("errors", message);
    }

    protected <T> ResponseEntity<T> json(T object, HttpStatus status) {
        return new ResponseEntity<>(object, status);
    }

    protected String getBindingErrorMessage(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        String message = "";
        for (FieldError error : errors) {
            message += error.getObjectName() + " - " + error.getDefaultMessage() + "\r\n";
        }
        return message;
    }

    /**
     * In view, must be tag to show this message <c:if test="${errors != null}">
     * <div class="alert alert-danger">
     * <p>
     * <spring:message code="ENTITY.save_error"></spring:message> ${errors}
     * </p>
     * </div> </c:if>
     */
}
