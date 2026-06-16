package com.hs.maritime.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hs.maritime.common.Result;
import com.hs.maritime.entity.User;
import com.hs.maritime.entity.UserProfile;
import com.hs.maritime.enums.UserStatusEnum;
import com.hs.maritime.exceptions.MaritimeException;
import com.hs.maritime.service.FileService;
import com.hs.maritime.service.UserProfileService;
import com.hs.maritime.service.UserService;
import com.hs.maritime.utils.JWTUtils;
import com.hs.maritime.utils.MD5Utils;
import com.hs.maritime.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private FileService fileService;

    @Resource
    private UserProfileService userProfileService;

    @Value("${file.avatar-dir}")
    private String avatarDir;

    /**
     * 修改用户的基本信息
     * */
    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody UserVO userVO){
        // 转换为User对象
        User user = new User();
        BeanUtils.copyProperties(userVO,user);

        // 更新用户的基本信息
        userService.updateById(user);

        // 返回结果
        return Result.success();
    }
    /**
     * 修改头像
     * */

    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(@RequestParam("file")MultipartFile file,
                                       @RequestParam("userId") Integer userId) throws IOException {
        // 上传头像图片
        String fileName = fileService.upload(file,avatarDir);
        // 生成访问该头像资源地址（企业：更换为在线图片服务器资源地址）
        String avatarUrl = "http://localhost:8080/api/download/avatar/" + fileName;
        // 更新数据用户头像
        // userService.update(new UpdateWrapper<User>().set("avatar",avatarUrl).eq("id",userId));
        userService.update(Wrappers.<User>update().set("avatar",avatarUrl).eq("id",userId));
        // 返回结果
        return Result.success(avatarUrl);
    }
    /**
     * 修改密码
     * */
    @PostMapping("/changePassword")
    public Result<String> changePassword(@RequestParam String password,@RequestParam String newPassword,@RequestHeader("Auth-Token")String authToken){
        // 获取授权Token令牌，获取用户编号
        Long userId = JWTUtils.getUserIdFromToken(authToken);

        // 根据用户编号，查询用户信息
        User dbUser = userService.getById(userId);

        // 验证旧密码是否正确
        String encryptedSubmitPassword = MD5Utils.MD5Encode(password + dbUser.getSalt(), "UTF-8");
        if(!StrUtil.equals(dbUser.getPassword(),encryptedSubmitPassword)){
          throw new MaritimeException("原密码错误");
        }
        // 验证新密码和旧密码是否一致
        if(StrUtil.equals(password,newPassword)){
            throw new MaritimeException("新密码不能和旧密码一致!");
        }
        // 更新密码
        userService.update(Wrappers.<User>update().set("password",MD5Utils.MD5Encode(newPassword+dbUser.getSalt(),"UTF-8")).eq("id",dbUser.getId()));

        // 返回结果
        return Result.success();
    }

    /**
     * 通知设置
     * */
    @PostMapping("/updateNotification")
    public Result<String> updateNotification(@RequestBody UserVO userVO,
                                             @RequestHeader("Auth-Token")String authToken){
        // 获取授权Token令牌，获取用户编号
        Long userId = JWTUtils.getUserIdFromToken(authToken);

        // 更新用户通知设置
        userProfileService.update(Wrappers.<UserProfile>update()
                .set("receive_due",userVO.getReceiveDue())
                .set("receive_email",userVO.getReceiveEmail())
                .set("receive_notice",userVO.getReceiveNotice()).eq("user_id",userId));

        // 返回结果
        return Result.success();
    }
    /**
     * 隐私设置
     * */
    @PostMapping("/updatePrivacy")
    public Result<String> updatePrivacy(@RequestBody UserVO userVO,
                                        @RequestHeader("Auth-Token")String authToken){
        // 获取授权Token令牌，获取用户编号
        Long userId = JWTUtils.getUserIdFromToken(authToken);

        // 更新用户隐私设置
        userProfileService.update(Wrappers.<UserProfile>update()
                .set("profile_visible",userVO.getProfileVisible())
                .set("borrow_his_visible",userVO.getBorrowHisVisible())
                .eq("user_id",userId));

        // 返回结果
        return Result.success();
    }
    /**
     * 注销账号
     * */
    @DeleteMapping("/off")
    public Result<String> updateUserOff(@RequestHeader("Auth-Token")String authToken){
        // 获取授权Token令牌，获取用户编号
        Long userId = JWTUtils.getUserIdFromToken(authToken);
        // 注销用户账号
        userService.update(Wrappers.<User>update().set("status", UserStatusEnum.OFFED.getCode()).eq("id",userId));
        // 返回结果
        return Result.success();
    }

    /**
     * 获取无读者证用户列表
     * 说明：如果用户数量比较多，直接查询，如果比较多，建议：不可以查询所有,而是前端根据输入的内容，动态查询+分页，标签是el-autocomplete
     * */
    @GetMapping("/withoutCardUsers")
    public Result<?> withoutCardUsers(){
        // 获取无读者证的所有用户
        List<User> withoutCardUserList = userService.getWithoutCardUsers();

        // 转换类型
        List<UserVO> userVOList = withoutCardUserList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user,userVO);
            return userVO;
        }).collect(Collectors.toList());

        // 返回结果
        return Result.success(userVOList);
    }
}
