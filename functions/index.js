const functions = require("firebase-functions");
const admin = require("firebase-admin");
const nodemailer = require("nodemailer");
require("dotenv").config();

admin.initializeApp();

const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: process.env.EMAIL,
    pass: process.env.EMAIL_PASSWORD,
  },
});

exports.sendOTPEmail = functions.https.onCall(async (data, context) => {
  const email = data.email;
  const otp = generateRandomOTP(); // Hàm tạo mã OTP ngẫu nhiên

  const mailOptions = {
    from: process.env.EMAIL,
    to: email,
    subject: "Your OTP for verification",
    text: `Your OTP for verification is: ${otp}`,
  };

  try {
    await transporter.sendMail(mailOptions);
    return {success: true, otp: otp};
  } catch (error) {
    console.error("Error sending email:", error);
    throw new functions.https.HttpsError("internal", "Email sending failed");
  }
});

/**
 * Generate a random 4-digit OTP
 * @return {string} The generated OTP
 */
function generateRandomOTP() {
  const otp = Math.floor(1000 + Math.random() * 9000);
  return otp.toString();
}
