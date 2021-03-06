from django.shortcuts import HttpResponse
import json
from datetime import datetime
from .models import UserSMS, UserGroup, Logs
from django.views.decorators.csrf import csrf_exempt
from django.db.models.query_utils import Q
from rest_framework.decorators import api_view
from django.core.validators import validate_email
from django import forms
from .serializer import LogsSerializer
from rest_framework import generics


@api_view(['POST'])
def user_logout(request):
    action = "Logged-Out"
    generateLogs(request, action)
    del request.session['user']
    del request.session['permissions']
    del request.session['usergroup']
    response_data = {}
    response_data['message'] = 'success'
    return HttpResponse(json.dumps(response_data), content_type="application/json")


@api_view(['POST'])
def user_login(request):
    response_data = {}
    errors = []
    if request.method == 'POST':
        email = request.POST.get('email')
        password = request.POST.get('password')
        if (email is None) or (len(email) == 0):
            errors.append("email: Enter Email-Id or Username")
        if (password is None) or (len(password) == 0):
            errors.append("password: Enter Password")
        if len(errors) == 0:
            # client does not know if it is username or email, so it sends any data in email field
            username = request.POST.get('email')
            try:  # to avoid error when no user exists
                user = UserSMS.objects.get(Q(email=email) | Q(username=username))
            except UserSMS.DoesNotExist:
                user = None
            # if user is None:
            #     response_data['message'] = 'fail'
            #     return HttpResponse(json.dumps(response_data), content_type="application/json")
            if user is not None:
                dbpass = user.password
                if dbpass == password:
                    #setting session variables
                    #-------------------------------------------------
                    request.session['user'] = user.username
                    group = user.group_name
                    permissions = UserGroup.objects.get(group_name=group)
                    temp={}
                    temp['user_permit'] = permissions.user_permit
                    temp['student_permit'] = permissions.student_permit
                    temp['hostel_permit'] = permissions.hostel_permit
                    temp['exam_permit'] = permissions.exam_permit
                    temp['review_permit'] = permissions.review_permit
                    request.session['permissions'] = temp
                    request.session['usergroup'] = group
                    #-------------------------------------------------
                    response_data['group_name'] = user.group_name
                    response_data['email'] = user.email
                    response_data['username'] = user.username
                    response_data['message'] = 'success'
                    action = "Logged-In"
                    generateLogs(request,action)
                else :
                    response_data['message'] = 'fail'
            else:
                response_data['message'] = 'fail'
        else:
            response_data['errors'] = errors
            response_data['message'] = 'fail'
    else:
        response_data['message'] = 'fail'
    return HttpResponse(json.dumps(response_data), content_type="application/json")


@api_view(['POST'])
def new_user_registration(request):
    response_data = {}
    var = request.session.get('permissions')['user_permit']
    if (var is not None) and (var == '2' or var == '3'):
        errors = []
        if request.method == 'POST':
            username = request.POST.get('username')
            if (username is None) or (len(username)==0):
                errors.append("username: Enter Username")
            existing = UserSMS.objects.get(username = username)
            if existing is not None:
                errors.append("User with this username already exists.")
            email = request.POST.get('email')
            if (email is None) or (len(email)==0):
                errors.append("email: Enter Email")
            else:
                try:
                    validate_email(email)
                except forms.ValidationError:
                    errors.append("email: Invalid email id")
            group_name = request.POST.get('group_name')
            if (group_name is None) or (len(group_name)==0):
                errors.append("group_name: Enter User Group")
            roll_no = request.POST.get('roll_no', '')
            password = request.POST.get('password')
            if (password is None) or (len(password)==0):
                errors.append("password: Enter Password")

            if len(errors) == 0:
                new_user = UserSMS()
                new_user.username = username
                new_user.email = email
                new_user.group_name = group_name
                new_user.password = password
                new_user.roll_no = roll_no
                new_user.save()
                response_data['message'] = 'success'
                action = "Created New User"
                generateLogs(request, action)
            else:
                response_data['errors'] = errors
                response_data['message'] = 'fail'
        else:
            response_data['message'] = 'fail'
    else:
        response_data['message'] = "You don't have permissions to create a new user."

    return HttpResponse(json.dumps(response_data), content_type="application/json")


@api_view(['POST'])
def new_usergroup(request):
    response_data = {}
    errors = []
    var = request.session.get('permissions')['user_permit']
    if (var is not None) and (var == '2' or var == '3'):
        if request.method == 'POST':
            group_name = request.POST.get('group_name')
            if (group_name is None) or (len(group_name)==0):
                errors.append("group_name: Enter Group Name")
            description = request.POST.get('description')
            if (description is None) or (len(description)==0):
                errors.append("description: Enter description")
            permissions = request.POST.get('permissions')
            if (permissions is None) or (len(permissions)==0):
                errors.append("permissions: Enter Permissions")

            if len(errors) == 0:
                new_group = UserGroup()
                new_group.group_name = group_name
                new_group.description = description
                new_group.permissions = permissions
                new_group.save()
                response_data['message'] = 'success'
                action = "Created New User-Group"
                generateLogs(request, action)
            else:
                response_data['errors'] = errors
                response_data['message'] = 'fail'
        else:
            response_data['message'] = 'fail'
    else:
        errors.append("Permission: You don't have permissions to create a new usergroup.")
        response_data['errors'] = errors
        response_data['message'] = 'fail'

    return HttpResponse(json.dumps(response_data), content_type="application/json")


class LogsList(generics.ListAPIView):
    queryset = Logs.objects.all()
    serializer_class = LogsSerializer


def generateLogs(request,action):
    logEntry = Logs()
    logEntry.uname = request.session.get('user')
    logEntry.ugroup = request.session.get('usergroup')
    logEntry.action = action
    logEntry.datetime = datetime.now()
    logEntry.ipAddress = get_client_ip(request)
    logEntry.system = request.META.get('HTTP_USER_AGENT', '')
    logEntry.save()


def get_client_ip(request):
    x_forwarded_for = request.META.get('HTTP_X_FORWARDED_FOR')
    if x_forwarded_for:
        ip = x_forwarded_for.split(',')[0]
    else:
        ip = request.META.get('REMOTE_ADDR')
    return ip


@api_view(['GET'])
def reviewPermit(request):
    response_data = {}
    var = request.session.get('permissions')['review_permit']
    if var=='1':
        response_data["permit"] = 'read'
    elif var=='2':
        response_data["permit"] = 'read+write'
    elif var=='3':
        response_data["permit"] = 'write'
    else:
        response_data["permit"] = 'no'
    response_data["message"] = "success"
    return HttpResponse(json.dumps(response_data), content_type="application/json")