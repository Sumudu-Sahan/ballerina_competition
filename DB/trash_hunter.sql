-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 22, 2017 at 01:43 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `trash_hunter`
--

-- --------------------------------------------------------

--
-- Table structure for table `garbage_collecting_point_details`
--

CREATE TABLE `garbage_collecting_point_details` (
  `garbage_collecting_point_id` int(11) NOT NULL,
  `garbage_collecting_point_name` varchar(255) DEFAULT NULL,
  `garbage_collecting_point_lat` varchar(255) DEFAULT NULL,
  `garbage_collecting_point_lon` varchar(255) DEFAULT NULL,
  `garbage_collecting_point_added_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `garbage_collecting_point_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive',
  `garbage_route_id` int(11) NOT NULL,
  `garbage_collecting_point_description` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `garbage_collecting_point_details`
--

INSERT INTO `garbage_collecting_point_details` (`garbage_collecting_point_id`, `garbage_collecting_point_name`, `garbage_collecting_point_lat`, `garbage_collecting_point_lon`, `garbage_collecting_point_added_datetime`, `garbage_collecting_point_active`, `garbage_route_id`, `garbage_collecting_point_description`) VALUES
(6, 'lop', '6.9339332843369', '79.84656035911', '2017-07-21 21:14:10', 1, 4, ''),
(7, 'lop', '6.9341122103139', '79.850667715291', '2017-07-21 21:14:10', 1, 4, ''),
(8, 'lop', '6.9344232008968', '79.844049811472', '2017-07-21 21:15:18', 1, 3, ''),
(9, 'lop', '6.934282616067', '79.843243360738', '2017-07-21 21:23:53', 1, 5, ''),
(10, 'lop', '6.934282616067', '79.847663641194', '2017-07-21 21:23:53', 1, 5, ''),
(11, 'lop', '6.9343252174956', '79.852470159749', '2017-07-21 21:23:53', 1, 5, '');

-- --------------------------------------------------------

--
-- Table structure for table `garbage_point_driver_tractor_merge_details`
--

CREATE TABLE `garbage_point_driver_tractor_merge_details` (
  `garbage_point_driver_tractor_merge_id` int(11) NOT NULL,
  `garbage_point_driver_tractor_merge_dtractor_id` int(11) NOT NULL,
  `garbage_point_driver_tractor_merge_point_id` int(11) NOT NULL,
  `garbage_point_driver_tractor_merge_status` int(1) NOT NULL DEFAULT '1' COMMENT '1 = pending, 2 = collected, 3 = rejected',
  `garbage_point_driver_tractor_merge_added_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `garbage_point_driver_tractor_merge_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive',
  `route_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `garbage_point_driver_tractor_merge_details`
--

INSERT INTO `garbage_point_driver_tractor_merge_details` (`garbage_point_driver_tractor_merge_id`, `garbage_point_driver_tractor_merge_dtractor_id`, `garbage_point_driver_tractor_merge_point_id`, `garbage_point_driver_tractor_merge_status`, `garbage_point_driver_tractor_merge_added_datetime`, `garbage_point_driver_tractor_merge_active`, `route_id`) VALUES
(1, 1, 6, 2, '2017-07-22 12:59:47', 1, 1),
(2, 1, 2, 1, '2017-07-22 12:59:47', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `garbage_post_details`
--

CREATE TABLE `garbage_post_details` (
  `garbage_post_id` int(11) NOT NULL,
  `garbage_post_user_id` int(11) NOT NULL,
  `garbage_post_title` varchar(255) DEFAULT NULL,
  `garbage_post_description` longtext,
  `garbage_post_image` varchar(255) DEFAULT NULL,
  `garbage_post_lat` varchar(255) DEFAULT NULL,
  `garbage_post_lon` varchar(255) DEFAULT NULL,
  `garbage_post_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 = pending, 2 = solved, 3 = rejected',
  `garbage_post_status_note` longtext NOT NULL,
  `garbage_post_added_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `garbage_post_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `garbage_post_details`
--

INSERT INTO `garbage_post_details` (`garbage_post_id`, `garbage_post_user_id`, `garbage_post_title`, `garbage_post_description`, `garbage_post_image`, `garbage_post_lat`, `garbage_post_lon`, `garbage_post_status`, `garbage_post_status_note`, `garbage_post_added_datetime`, `garbage_post_active`) VALUES
(1, 1, 'Test 01', 'Hi I am here. Please help me!', '', '6.908159', ' 79.852859', 1, '', '2017-07-21 23:19:49', 1),
(2, 2, 'bdh', 'bdhdjd', '27:21:46postIMG.jpg', '6.909857', '79.8525413', 1, '', '2017-07-22 12:51:57', 1),
(3, 2, 't st report', 'ehdudhdjd', '28:42:36postIMG.jpg', '6.909857', '79.8525413', 1, '', '2017-07-22 14:13:23', 1);

-- --------------------------------------------------------

--
-- Table structure for table `garbage_route_name`
--

CREATE TABLE `garbage_route_name` (
  `route_id` int(11) NOT NULL,
  `route_name` varchar(255) NOT NULL,
  `route_description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `garbage_route_name`
--

INSERT INTO `garbage_route_name` (`route_id`, `route_name`, `route_description`) VALUES
(3, 'test1', 'hi'),
(4, 'test2', 'hi'),
(5, 'test3', 'hi'),
(6, 'test4', 'hi');

-- --------------------------------------------------------

--
-- Table structure for table `notification_details`
--

CREATE TABLE `notification_details` (
  `notification_id` int(11) NOT NULL,
  `notification_mainValue` varchar(255) DEFAULT NULL,
  `notification_subValue1` varchar(255) DEFAULT NULL,
  `notification_subValue2` varchar(255) DEFAULT NULL,
  `notification_subValue3` varchar(255) DEFAULT NULL,
  `notification_subValue4` varchar(255) DEFAULT NULL,
  `notification_added_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `notification_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `tractor_details`
--

CREATE TABLE `tractor_details` (
  `tractor_id` int(11) NOT NULL,
  `tractor_image` varchar(255) DEFAULT NULL,
  `tractor_added_date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tractor_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 = active, 0 = inactive',
  `tractor_number` varchar(50) NOT NULL,
  `tractor_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 = recycler, 2 = non-recycler',
  `tractor_lat` varchar(255) DEFAULT NULL,
  `tractor_lon` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractor_details`
--

INSERT INTO `tractor_details` (`tractor_id`, `tractor_image`, `tractor_added_date_time`, `tractor_active`, `tractor_number`, `tractor_type`, `tractor_lat`, `tractor_lon`) VALUES
(1, 'https://forum.vectorworks.net/uploads/monthly_2017_03/19-2924T_1-WM-Loader-134-First-Gear.jpg.b47a0da1158890db5c89970bcdf79a77.jpg', '2017-07-17 20:19:08', 1, '123456', 1, '6.910630', '79.856678'),
(2, 'https://forum.vectorworks.net/uploads/monthly_2017_03/19-2924T_1-WM-Loader-134-First-Gear.jpg.b47a0da1158890db5c89970bcdf79a77.jpg', '2017-07-17 20:22:09', 1, 'WP XE 4568', 2, '6.908159', '79.852859');

-- --------------------------------------------------------

--
-- Table structure for table `tractor_driver_merge_details`
--

CREATE TABLE `tractor_driver_merge_details` (
  `tractor_driver_merge_id` int(11) NOT NULL,
  `tractor_driver_merge_driver_id` int(11) NOT NULL,
  `tractor_driver_merge_tractor_id` int(11) NOT NULL,
  `tractor_driver_merge_added_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `tractor_driver_merge_active` int(1) NOT NULL DEFAULT '1' COMMENT '1=active, 0=inactive'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tractor_driver_merge_details`
--

INSERT INTO `tractor_driver_merge_details` (`tractor_driver_merge_id`, `tractor_driver_merge_driver_id`, `tractor_driver_merge_tractor_id`, `tractor_driver_merge_added_datetime`, `tractor_driver_merge_active`) VALUES
(1, 1, 1, '2017-07-22 12:59:07', 1),
(2, 1, 2, '2017-07-22 12:59:07', 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_details`
--

CREATE TABLE `user_details` (
  `user_id` int(11) NOT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_name` varchar(250) DEFAULT NULL,
  `user_role` int(1) NOT NULL DEFAULT '1' COMMENT '1 = people, 2 = MC, 3 = Tractor',
  `user_account_type` tinyint(1) NOT NULL DEFAULT '3' COMMENT '1 = FB, 2= G+, 3 = Normal',
  `user_password` varchar(255) DEFAULT NULL,
  `user_device_token` longtext,
  `user_added_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1 = active,0 = inactive',
  `user_image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_details`
--

INSERT INTO `user_details` (`user_id`, `user_email`, `user_name`, `user_role`, `user_account_type`, `user_password`, `user_device_token`, `user_added_datetime`, `user_active`, `user_image`) VALUES
(1, 's@m.com', 'Sumudu Sahan', 3, 3, '202cb962ac59075b964b07152d234b70', 'f2B7OPBLOK0:APA91bGrvGLjP-vdycmh_K9xGeRgnfCoHX9lXQsgQz0rVR-IEKwaMeQXvEjL_1G-jKxGeQJL0Nb2g2oEv8Yi2XNVP8gDNeM8x2ddAyFoNhTC4fIylUwUsawbM0u1nry3zQmK_Xb7o-ws', '2017-07-17 10:33:54', 1, NULL),
(2, 'sumudusahan94@gmail.com', 'Sumudu Sahan Weerasuriya', 1, 1, NULL, 'f2B7OPBLOK0:APA91bGrvGLjP-vdycmh_K9xGeRgnfCoHX9lXQsgQz0rVR-IEKwaMeQXvEjL_1G-jKxGeQJL0Nb2g2oEv8Yi2XNVP8gDNeM8x2ddAyFoNhTC4fIylUwUsawbM0u1nry3zQmK_Xb7o-ws', '2017-07-17 10:42:00', 1, 'https://graph.facebook.com/1882072398780085/picture?type=large'),
(3, 'sumudusahanmax@gmail.com', 'Sumudu Sahan', 1, 2, NULL, 'crKUn95yiTE:APA91bFuXFtBgKNdG8GRTuhS__myuGzhLuFTQX4bu6GKaPjQNgL0xBFf-2P29od5tCx8P76h1dEvTF67ulPeP9a6iPGfs71dyI9GWIFe2JE_SPQMTQ9eIaklcDh5jsxnilnmt5hiCNEY', '2017-07-17 11:06:47', 1, 'https://lh5.googleusercontent.com/-r8QJlaaw0Bc/AAAAAAAAAAI/AAAAAAAAAvo/9CDpFGnVfdM/photo.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `garbage_collecting_point_details`
--
ALTER TABLE `garbage_collecting_point_details`
  ADD PRIMARY KEY (`garbage_collecting_point_id`);

--
-- Indexes for table `garbage_point_driver_tractor_merge_details`
--
ALTER TABLE `garbage_point_driver_tractor_merge_details`
  ADD PRIMARY KEY (`garbage_point_driver_tractor_merge_id`);

--
-- Indexes for table `garbage_post_details`
--
ALTER TABLE `garbage_post_details`
  ADD PRIMARY KEY (`garbage_post_id`);

--
-- Indexes for table `garbage_route_name`
--
ALTER TABLE `garbage_route_name`
  ADD PRIMARY KEY (`route_id`);

--
-- Indexes for table `notification_details`
--
ALTER TABLE `notification_details`
  ADD PRIMARY KEY (`notification_id`);

--
-- Indexes for table `tractor_details`
--
ALTER TABLE `tractor_details`
  ADD PRIMARY KEY (`tractor_id`);

--
-- Indexes for table `tractor_driver_merge_details`
--
ALTER TABLE `tractor_driver_merge_details`
  ADD PRIMARY KEY (`tractor_driver_merge_id`);

--
-- Indexes for table `user_details`
--
ALTER TABLE `user_details`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `garbage_collecting_point_details`
--
ALTER TABLE `garbage_collecting_point_details`
  MODIFY `garbage_collecting_point_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `garbage_point_driver_tractor_merge_details`
--
ALTER TABLE `garbage_point_driver_tractor_merge_details`
  MODIFY `garbage_point_driver_tractor_merge_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `garbage_post_details`
--
ALTER TABLE `garbage_post_details`
  MODIFY `garbage_post_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `garbage_route_name`
--
ALTER TABLE `garbage_route_name`
  MODIFY `route_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `notification_details`
--
ALTER TABLE `notification_details`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `tractor_details`
--
ALTER TABLE `tractor_details`
  MODIFY `tractor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tractor_driver_merge_details`
--
ALTER TABLE `tractor_driver_merge_details`
  MODIFY `tractor_driver_merge_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `user_details`
--
ALTER TABLE `user_details`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
