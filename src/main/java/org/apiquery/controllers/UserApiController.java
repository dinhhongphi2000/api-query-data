package org.apiquery.apiControllers.v1;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apiquery.dtos.ResponseStatusDTO;
import org.apiquery.shared.data.*;
import org.apiquery.shared.utils.AdvanceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apiquery.services.*;

@Controller
@RequestMapping(path = "/api/v1/user")
public class UserApiController extends AdvanceController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<ResponseStatusDTO> getList(Pageable pageable, QueryFilter filter, QueryInclude includes,
            HttpServletRequest httpRequest) {
        try {
            return success(userService.findAll(pageable, filter, includes.getFields()));
        } catch (Exception ex) {
            return fail(ex, httpRequest);
        }
    }

    @PostMapping("advance-search")
    public ResponseEntity<ResponseStatusDTO> advanceSearch(Pageable pageable, QueryAdvanceFilter filter,
            QueryInclude includes, HttpServletRequest httpRequest) {
        try {
            return success(userService.findAll(pageable, filter, includes.getFields()));
        } catch (Exception ex) {
            return fail(ex, httpRequest);
        }
    }
}