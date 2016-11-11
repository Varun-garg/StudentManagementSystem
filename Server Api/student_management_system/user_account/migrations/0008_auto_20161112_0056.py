# -*- coding: utf-8 -*-
# Generated by Django 1.9.8 on 2016-11-11 19:26
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('user_account', '0007_auto_20161112_0006'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='usersms',
            options={},
        ),
        migrations.AlterModelManagers(
            name='usersms',
            managers=[
            ],
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='date_joined',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='first_name',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='groups',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='is_active',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='is_staff',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='is_superuser',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='last_login',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='last_name',
        ),
        migrations.RemoveField(
            model_name='usersms',
            name='user_permissions',
        ),
        migrations.AlterField(
            model_name='usersms',
            name='email',
            field=models.EmailField(max_length=200, null=True),
        ),
        migrations.AlterField(
            model_name='usersms',
            name='password',
            field=models.CharField(max_length=200),
        ),
        migrations.AlterField(
            model_name='usersms',
            name='username',
            field=models.CharField(max_length=10, null=True),
        ),
    ]
