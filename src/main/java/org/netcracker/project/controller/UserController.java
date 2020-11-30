package org.netcracker.project.controller;

import lombok.RequiredArgsConstructor;
import org.netcracker.project.model.User;
import org.netcracker.project.model.dto.SimpleTeam;
import org.netcracker.project.model.enums.Result;
import org.netcracker.project.model.enums.TeamRole;
import org.netcracker.project.service.UserService;
import org.netcracker.project.util.StatisticsUtil;
import org.netcracker.project.util.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final StatisticsUtil statisticsUtil;
    private final UserService userService;

    @GetMapping("/{id}")
    public String getUser(
            @PathVariable("id") User user,
            Model model
    ){
        statisticsUtil.putStatisticsInModel(user, model);
        return "user";
    }

    @PutMapping("/{id}")
    public String updateUser(
            @AuthenticationPrincipal User authUser,
            @RequestParam("password2") String password2,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ){
        if (!user.getId().equals(authUser.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        boolean passwordsEqual = password2.equals(user.getPassword());
        if (bindingResult.hasErrors() || !passwordsEqual) {
            Map<String, String> errors = ValidationUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            if (!passwordsEqual) model.addAttribute("passwordDiffError", "Passwords are different");
            return "redirect:/user/{id}";
        }
        userService.updateUser(authUser, user, password2);
        return "redirect:/user/{id}";
    }

    @PutMapping("/{id}/roles")
    @ResponseBody
    public void updateUserRoles(
            @AuthenticationPrincipal User authUser,
            @PathVariable("id") User user,
            @RequestBody Set<TeamRole> roles
    ) {
        if (!authUser.getId().equals(user.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        userService.updateUserRoles(user, roles);
    }

    @PutMapping("/{id}/image")
    @ResponseBody
    public void updateAvatar(
            @AuthenticationPrincipal User authUser,
            @PathVariable("id") User user,
            @RequestParam(value = "x", required = false) Integer x,
            @RequestParam(value = "y", required = false) Integer y,
            @RequestParam(value = "width", required = false) Integer width,
            @RequestParam(value = "height", required = false) Integer height,
            @RequestParam("avatar") MultipartFile avatar
    ) throws IOException {
        if (!authUser.getId().equals(user.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (x == null) {
            userService.saveAvatar(user, avatar);
        } else {
            userService.cropAndSaveAvatar(user, avatar, x, y, width, height);
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(
            @AuthenticationPrincipal User authUser,
            @PathVariable("id") User user,
            @RequestParam("password2") String password2,
            Model model
    ){
        if (!user.getId().equals(authUser.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        if (BCrypt.checkpw(password2, user.getPassword())) {
            model.addAttribute("passwordDiffError", "Passwords are different");
            return "redirect:/user/{id}";
        }
        userService.deleteUser(user);
        return "redirect:/login";
    }

    @GetMapping("/name/{id}")
    @ResponseBody
    public String getName(@PathVariable("id") User user) {
        return user.getSurname() + " " + user.getName() + " " + user.getSecName() + " (" + user.getUsername() + ")";
    }

    @GetMapping(value = "/team/{id}", produces = "application/json")
    @ResponseBody
    public Set<SimpleTeam> getTeams(@PathVariable("id") User user) {
        return user.getTeams().stream().map(SimpleTeam::of).collect(Collectors.toSet());
    }
}