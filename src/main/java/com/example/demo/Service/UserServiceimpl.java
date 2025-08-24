package com.example.demo.Service;

import com.example.demo.utility.OtpPurpose;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.UserProfileUpdateDTO;
import com.example.demo.Dto.UserSignupDTO;
import com.example.demo.Entity.OtpVerification;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserServiceException;
import com.example.demo.Repository.OtpVerificationRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class UserServiceimpl implements UserService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpVerificationRepository otpRepository;   // ✅ only keep this
    
    @Autowired
    private EmailService emailService;

    // -------------------- SIGNUP ----------------------
    @Override
    public String signup(UserSignupDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return "Email already exists!";
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            return "Username already exists!";
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setMobileNo(dto.getMobileNo());
        user.setCreateDate(LocalDate.now());
        user.setIsEmailVerified(false);
        userRepository.save(user);

        // ✅ Generate OTP for signup verification
        OtpVerification otp = new OtpVerification();
        otp.setEmail(dto.getEmail());
        otp.setOtp(generateOtp());
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        otp.setExpired(false);
        otp.setPurpose(OtpPurpose.SIGNUP_VERIFY);
        otpRepository.save(otp);

        emailService.sendOtp(dto.getEmail(), otp.getOtp());

        return "Signup successful! Please verify your email.";
    }

    // -------------------- UPDATE PROFILE ----------------------
    @Override
    public String updateProfile(UserProfileUpdateDTO dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        if (optionalUser.isEmpty()) {
            return "User not found!";
        }

        User user = optionalUser.get();

        // 👇 Block updates if signup email not verified
        if (!user.getIsEmailVerified()) {
            return "Email not verified. Please verify your email before updating profile.";
        }

        // ✅ Check if username belongs to someone else
        Optional<User> existingUserWithSameUsername = userRepository.findByUsername(dto.getUsername());
        if (existingUserWithSameUsername.isPresent() &&
                !existingUserWithSameUsername.get().getUserId().equals(dto.getUserId())) {
            return "Username already exists!";
        }

        // ✅ If email is being changed → verify new email first
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                return "New email already in use!";
            }

            // Save pending email
            user.setPendingEmail(dto.getEmail());
            userRepository.save(user);

            // Generate OTP for EMAIL_CHANGE
            OtpVerification otp = new OtpVerification();
            otp.setEmail(dto.getEmail());
            otp.setOtp(generateOtp());
            otp.setExpiryTime(LocalDateTime.now().plusMinutes(10));
            otp.setExpired(false);
            otp.setPurpose(OtpPurpose.EMAIL_CHANGE);
            otpRepository.save(otp);

            emailService.sendOtp(dto.getEmail(), otp.getOtp());

            return "OTP sent to new email. Please verify before updating.";
        }

        // ✅ Update other profile fields normally
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAddress(dto.getAddress());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setCreateDate(dto.getCreateDate());
        user.setEducation(dto.getEducation());
        user.setPassoutYear(dto.getPassoutYear());
        user.setStatus(dto.getStatus());
        user.setParentContactNo(dto.getParentContactNo());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setDob(dto.getDob());
        user.setAadharCard(dto.getAadharCard());
        user.setUanNo(dto.getUanNo());

        userRepository.save(user);
        return "Profile updated successfully!";
    }

    // -------------------- VERIFY OTP ----------------------
    public String verifyOtp(String email, String otp, OtpPurpose purpose) {
        Optional<OtpVerification> otpData = otpRepository.findByEmailAndOtpAndPurpose(email, otp, purpose);

        if (otpData.isEmpty()) {
            return "Invalid OTP!";
        }

        OtpVerification otpEntity = otpData.get();

        if (otpEntity.isExpired() || LocalDateTime.now().isAfter(otpEntity.getExpiryTime())) {
            otpEntity.setExpired(true);
            otpRepository.save(otpEntity);
            return "OTP expired!";
        }

        // mark OTP as used
        otpEntity.setExpired(true);
        otpRepository.save(otpEntity);

        // ✅ Handle different OTP purposes
        if (purpose == OtpPurpose.SIGNUP_VERIFY) {
            Optional<User> userData = userRepository.findByEmail(email);
            if (userData.isPresent()) {
                User user = userData.get();
                user.setIsEmailVerified(true);
                userRepository.save(user);
            }
            return "Signup email verified successfully!";
        }

        if (purpose == OtpPurpose.EMAIL_CHANGE) {
            Optional<User> userData = userRepository.findByPendingEmail(email);
            if (userData.isPresent()) {
                User user = userData.get();
                user.confirmNewEmail(); // moves pendingEmail → email
                userRepository.save(user);
            }
            return "Email updated and verified successfully!";
        }

        return "OTP verified!";
    }

    // -------------------- DELETE / GET ----------------------
    public void deleteUserById(Long id) throws UserServiceException {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserServiceException("User not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public void deleteUserByUsername(String username) throws UserServiceException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserServiceException("User not found with username: " + username,HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user.get());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new UserServiceException("User not found with ID: " + id , HttpStatus.NOT_FOUND)
        );
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
            () -> new UserServiceException("User not found with username: " + username , HttpStatus.NOT_FOUND)
        );
    }

    // Utility method to generate 6-digit OTP
    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
