# Generated by Django 2.1.3 on 2018-11-23 05:06

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('userprofile', '0003_auto_20181116_2007'),
        ('Matchmaking', '0004_auto_20181024_1439'),
    ]

    operations = [
        migrations.RenameField(
            model_name='match',
            old_name='accepted',
            new_name='user1HasMatched',
        ),
        migrations.AddField(
            model_name='match',
            name='group_members',
            field=models.ManyToManyField(to='userprofile.Profile'),
        ),
        migrations.AddField(
            model_name='match',
            name='group_name',
            field=models.CharField(default='NULL', max_length=100),
        ),
        migrations.AddField(
            model_name='match',
            name='user1accepted',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='match',
            name='user2HasMatched',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='match',
            name='user2accepted',
            field=models.BooleanField(default=False),
        ),
        migrations.AlterField(
            model_name='match',
            name='score',
            field=models.IntegerField(default=-1),
        ),
    ]