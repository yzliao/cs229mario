% PlotScore.m
clear all
close all
clc


load eval.txt

x1 = 0:20:20*9;
x2 = 150:50:500;
x3 = 600:100:1e3;

x = [x1,x2,x3];

figure(1)
plot(x1,eval(:,1),'b-o')
%V = axis;
%V(3) = 0;
%axis(V)
grid on


figure(2)
plot(x1,eval(:,2),'r-o');
hold on
plot(x1,eval(:,3),'k-h');
plot(x1,eval(:,4),'g-s');
grid on

figure(3)
plot(x1,eval(:,5),'y-<');

grid on