from django.shortcuts import HttpResponse
import json
from .models import UserSMS
from django.views.decorators.csrf import csrf_exempt
from django.db.models.query_utils import Q
from rest_framework.decorators import api_view


@api_view(['POST'])
def user_login(request):
    response_data = {}

    if request.method == 'POST':
        username = request.POST.get('email', '')
        # client does not know if it is username or email, so it sends any data in email field
        email = request.POST.get('email', '')
        password = request.POST.get('password', '')

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

                response_data['group_name'] = user.group_name
                response_data['email'] = user.email
                response_data['username'] = user.username
                response_data['roll_no'] = user.roll_no
                response_data['message'] = 'success'

            else:
                response_data['message'] = 'fail'
        else:
            response_data['message'] = 'fail'
    else:
        response_data['message'] = 'fail'
    return HttpResponse(json.dumps(response_data), content_type="application/json")


@api_view(['POST'])
def new_user_registration(request):
    response_data = {}
    errors = []
    if request.method == 'POST':
        username = request.POST.get('username')
        print(username)
        if username is None:
            errors.append("username: Enter Username")
        email = request.POST.get('email')
        if email is None:
            errors.append("email: Enter Email")
        group_name = request.POST.get('group_name')
        if group_name is None:
            errors.append("group_name: Enter User Group")
        roll_no = request.POST.get('roll_no', '')
        password = request.POST.get('password')
        if password is None:
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
        else:
            response_data['errors'] = errors
            response_data['message'] = 'fail'
    else:
        response_data['message'] = 'fail'

    return HttpResponse(json.dumps(response_data), content_type="application/json")