# Generated by Django 2.1.3 on 2018-11-25 03:45

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Matchmaking', '0006_auto_20181124_1938'),
    ]

    operations = [
        migrations.AlterField(
            model_name='match',
            name='user1',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.CASCADE, related_name='user1', to='userprofile.Profile'),
        ),
    ]
