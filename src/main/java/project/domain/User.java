package project.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.*;
import static java.util.stream.Collectors.toList;

import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name = "users")
@Getter
public class User extends BaseEntity{
    
    private static final String DEFAULT_PROFILE_URL = "";
    
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(unique = true, nullable = true)
    private String nickName;
    
    @Column(nullable = true)
    private String password;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = true)
    private String profile;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;
    
    //<== 유저 객체 삭제용 ==>
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces = new ArrayList<> ();
    
    @OneToMany(mappedBy = "user",  orphanRemoval = true)
    private List<UserSchedule> userSchedules = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<DevLog> devLogs = new ArrayList<>();
    
    // <== 생성자 ==>
    
    // JPA용 생성자
    protected User () {} 
    
    @Builder //builder
    public User(String email, String nickName, String password, String name, String profile, UserRole userRole){        
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.name = name;
        this.profile = profile;
        this.userRole = userRole;
    }
    
    // <== 정적 팩토리 메서드 ==>
    public static User fromOAuth2Attributes(Map<String,Object> attributes){
        return User.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .profile(User.DEFAULT_PROFILE_URL)
            .userRole(UserRole.USER)
            .build();
    }
    
    public static User ofEmailPassword(String email, String password, String name, PasswordEncoder passwordEncoder){
        
        User user =  User.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .profile(User.DEFAULT_PROFILE_URL)
                        .userRole(UserRole.USER)
                        .build();
        
        user.encodePassword(passwordEncoder);
        return user;
    }
    
    
    // <== 비즈니스 로직 ==>
    public String getRoleKey(){
        return this.userRole.getKey();
    }
    
    public boolean checkFinishSignUp(){
        return StringUtils.hasText(this.nickName);
    }
    
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }
    
    public boolean checkPassword(String password, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(password, this.password);
    }
    
    public void updateNickName(String nickName){
        this.nickName = nickName;
    }
    
    public void update(String nickName, String password, String name, String profile, PasswordEncoder passwordEncoder){
        this.nickName = nickName;
        this.password = password;
        this.name = name;
        this.profile = profile;
        
        encodePassword(passwordEncoder);
    }
}