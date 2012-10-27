# -*- coding: utf-8 -*-
"""
Created on Tue Jul 31 12:16:43 2012

@author: Fesh0r
@version: v1.0
"""

import sys
import logging
from optparse import OptionParser

from commands import Commands, CLIENT, SERVER
from mcp import updateids_side


def main():
    parser = OptionParser(version='MCP %s' % Commands.fullversion())
    parser.add_option('-f', '--force', action='store_true', dest='force', help='force update', default=False)
    parser.add_option('-c', '--config', dest='config', help='additional configuration file')
    options, _ = parser.parse_args()
    updateids(options.config, options.force)


def updateids(conffile, force):
    try:
        commands = Commands(conffile)

        if not force:
            print 'WARNING:'
            print 'The updateids script is unsupported, not recommended, and can break your'
            print 'code in hard to detect ways.'
            print 'Only use this script if you absolutely know what you are doing, or when a'
            print 'MCP team member asks you to do so.'
            answer = raw_input('If you really want to update all classes, enter "Yes" ')
            if answer.lower() not in ['yes']:
                print 'You have not entered "Yes", aborting the update process'
                sys.exit(1)

        updateids_side(commands, CLIENT)
        updateids_side(commands, SERVER)
    except Exception:  # pylint: disable-msg=W0703
        logging.exception('FATAL ERROR')
        sys.exit(1)


if __name__ == '__main__':
    main()
